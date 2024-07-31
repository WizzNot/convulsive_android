package com.example.convulsive;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;
import static com.example.convulsive.server_data.ServiceConstructor.CreateService;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.convulsive.server_data.CheckoutData;
import com.example.convulsive.server_data.Config;
import com.example.convulsive.server_data.Server;
import com.example.convulsive.server_data.ServiceConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private ImageButton cartButton;
    private HashMap<ProductCart, Integer> cart = new HashMap<>();
    private boolean cart_is_visible = false;

    // В MainActivity

    private void ifCartIsEmpty(Dialog dialog){
        dialog.dismiss();
        cart_is_visible=false;
        cartButton.setVisibility(View.GONE);
    }
    private void populateCart(Dialog dialog) { // method for a cart creating
        final long[] totalPrice = {0};
        LinearLayout cartItemContainer = dialog.findViewById(R.id.cart_item_container);

        // Удаляем все существующие карточки товаров
        cartItemContainer.removeAllViews();

        for (Map.Entry<ProductCart, Integer> entry : cart.entrySet()) {
            ProductCart productCart = entry.getKey();
            final int[] quantity = {entry.getValue()};

            // Создаем карточку товара
            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item_product, null);

            // Заполняем данные карточки товара
            ImageView itemImage = cartItemView.findViewById(R.id.cart_itemimage);
            itemImage.setImageResource(productCart.getImageResource());

            TextView itemName = cartItemView.findViewById(R.id.cart_itemname);
            itemName.setText(productCart.getName());

            TextView itemPrice = cartItemView.findViewById(R.id.cart_itemprice);
            itemPrice.setText(String.valueOf(productCart.getPrice() * quantity[0]) + "Р");
            totalPrice[0] = totalPrice[0] + productCart.getPrice() * quantity[0];

            TextView itemCount = cartItemView.findViewById(R.id.cart_countofitem);
            itemCount.setText(String.valueOf(quantity[0]));

            TextView itemSize = cartItemView.findViewById(R.id.cart_itemsize);
            itemSize.setText(productCart.getSize());
            // Order Price
            TextView orderPrice = dialog.findViewById(R.id.cart_orderprice);
            orderPrice.setText("TOTAL PRICE: " + String.valueOf(totalPrice[0]) + "P");
            // Buttons
            Button decreaseButton = cartItemView.findViewById(R.id.cart_buttonDecrease);
            Button increaseButton = cartItemView.findViewById(R.id.cart_buttonIncrease);
            Button deleteItemButton = cartItemView.findViewById(R.id.cart_buttondelete);
            // Listeners
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity[0]--;
                    cart.put(productCart, quantity[0]);
                    totalPrice[0] = totalPrice[0] - productCart.getPrice();
                    orderPrice.setText("TOTAL PRICE: " + String.valueOf(totalPrice[0]) + "P");
                    if(quantity[0] <= 0){
                        cartItemContainer.removeView(cartItemView);
                        cart.remove(productCart);
                        if(cart.size() == 0){
                            ifCartIsEmpty(dialog);
                        }
                    }
                    else{
                        itemPrice.setText(String.valueOf(productCart.getPrice() * quantity[0]) + "P");
                        itemCount.setText(String.valueOf(quantity[0]));
                    }
                }
            });
            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalPrice[0] = totalPrice[0] + productCart.getPrice();
                    orderPrice.setText("TOTAL PRICE: " + String.valueOf(totalPrice[0]) + "P");
                    quantity[0]++;
                    cart.put(productCart, quantity[0]);
                    itemPrice.setText(String.valueOf(productCart.getPrice() * quantity[0]) + "P");
                    itemCount.setText(String.valueOf(quantity[0]));
                }
            });

            deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalPrice[0] = totalPrice[0] - quantity[0] * productCart.getPrice();
                    orderPrice.setText("TOTAL PRICE: " + String.valueOf(totalPrice[0]) + "P");
                    cartItemContainer.removeView(cartItemView);
                    cart.remove(productCart);
                    if(cart.size() == 0){
                        ifCartIsEmpty(dialog);
                    }
                }
            });
            // EditTexts
            EditText userName = dialog.findViewById(R.id.cart_username);
            EditText email = dialog.findViewById(R.id.cart_usermail);
            EditText phoneNumber = dialog.findViewById(R.id.cart_userphone);
            // Checkout Button
            Button checkoutButton = dialog.findViewById(R.id.cart_checkoutButton);
            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = userName.getText().toString();
                    String user_email = email.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    if (name.isEmpty() || user_email.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(dialog.getContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int price = (int)totalPrice[0];
                    List<String> items = new ArrayList<>();
                    List<ProductCart> cartKeys = new ArrayList<ProductCart>(cart.keySet());
                    for(int i=0; i< cartKeys.size(); i++){
                        items.add(cartKeys.get(i).getName() + " " + cartKeys.get(i).getSize() + ": " + String.valueOf(cart.get(cartKeys.get(i))));
                    }
                    CheckoutData checkoutData = new CheckoutData(name, user_email, phone, String.join("\n", items), price);

                    // making request
                    Call<CheckoutData> call = CreateService(Server.class, Config.SERVER_URL).checkout(checkoutData);
                    call.enqueue(new Callback<CheckoutData>() {
                        @Override
                        public void onResponse(Call<CheckoutData> call, Response<CheckoutData> response) {
                            if (response.isSuccessful()) {
                                // Успешный ответ, вы можете обработать данные
                                Log.d("post", "success");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(dialog.getContext(), "Success", Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.dismiss();
                                cart = new HashMap<>();
                                cartButton.setVisibility(View.GONE);
                                cart_is_visible = false;
                            } else {
                                // Неуспешный ответ, вы можете обработать код ошибки
                                Log.d("post", "failure");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(dialog.getContext(), "Failure", Toast.LENGTH_LONG).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<CheckoutData> call, Throwable t) {
                            Log.d("post", "failure");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(dialog.getContext(), "Failure", Toast.LENGTH_LONG).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                }
            });
            // Добавляем карточку товара в контейнер
            cartItemContainer.addView(cartItemView);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // cartButton
        cartButton = findViewById(R.id.cartButton);
        if(cart_is_visible) cartButton.setVisibility(View.VISIBLE);
        else cartButton.setVisibility(View.GONE);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cartButton", "wtf");
                Dialog cartDialog = new Dialog(MainActivity.this);
                cartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cartDialog.setContentView(R.layout.cart_bottomsheet);
                populateCart(cartDialog);
                cartDialog.show();
                cartDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                cartDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                cartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cartDialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
        // Contact Button
        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Contact button is pressed");
                Dialog contactDialog = new Dialog(MainActivity.this);
                contactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                contactDialog.setContentView(R.layout.contact_bottomsheet);
                // Getting user info
                EditText name = contactDialog.findViewById(R.id.contact_name);
                EditText phone = contactDialog.findViewById(R.id.contact_phone);
                Button contact_send = contactDialog.findViewById(R.id.contact_sendbutton);
                // listener
                contact_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = name.getText().toString();
                        String userPhone = phone.getText().toString();
                        if (username.isEmpty() || userPhone.isEmpty()) {
                            Toast.makeText(contactDialog.getContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.d("contact", username + " " + userPhone);
                        CheckoutData contactData = new CheckoutData(username, userPhone);
                        // making request
                        Call<CheckoutData> call = CreateService(Server.class, Config.SERVER_URL).contact(contactData);
                        call.enqueue(new Callback<CheckoutData>() {
                            @Override
                            public void onResponse(Call<CheckoutData> call, Response<CheckoutData> response) {
                                if(response.isSuccessful()){
                                    Log.d("Contact", "Success");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(contactDialog.getContext(), "Success", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    contactDialog.dismiss();
                                }
                                else{
                                    Log.d("Contact", "Failure");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(contactDialog.getContext(), "Failure", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckoutData> call, Throwable t) {
                                Log.d("Contact", "Failure");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(contactDialog.getContext(), "Failure", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }
                });
                // Showing dialog
                contactDialog.show();
                contactDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                contactDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                contactDialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
        // generate products
        recyclerView = findViewById(R.id.recyclerView);
        productList = Product.generateProductList();
        productAdapter = new ProductAdapter(productList);
        // adapter
        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.item_bottomsheet);
                // Setting text
                TextView itemName = dialog.findViewById(R.id.dialog_itemname);
                TextView itemDescription = dialog.findViewById(R.id.dialog_itemdescription);
                TextView itemPrice = dialog.findViewById(R.id.dialog_itemprice);
                itemName.setText(product.getName());
                itemDescription.setText(product.getDescription());
                itemPrice.setText(String.valueOf(product.getPrice()) + "P");
                // Setting images
                int[] images = product.getImages();
                ViewPager2 viewPager = dialog.findViewById(R.id.view_pager);
                ImagesAdapter adapter = new ImagesAdapter(images);
                viewPager.setAdapter(adapter);
                // sizes
                Spinner sizesSpinner = dialog.findViewById(R.id.spinner_size);
                if (product.getSizes() != null && product.getSizes().length > 0) {
                    sizesSpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> sizesadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, product.getSizes());
                    sizesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sizesSpinner.setAdapter(sizesadapter);
                }
                // adding to cart
                Button addButton = dialog.findViewById(R.id.item_addbutton);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String size = "";
                        if(product.getSizes().length > 0){
                            size = sizesSpinner.getSelectedItem().toString();
                        }
                        ProductCart newProductcart = new ProductCart(product.getName(), product.getPrice(), product.getImageResource(), size);
                        if(!cart.containsKey(newProductcart)){
                            cart.put(newProductcart, 1);
                        }
                        else{
                            cart.put(newProductcart, cart.get(newProductcart) + 1);
                        }
                        if (!cart_is_visible) {
                            cart_is_visible = true;
                            Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                            cartButton.startAnimation(fadeInAnimation);
                            cartButton.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(dialog.getContext(), "added to cart", Toast.LENGTH_LONG);
                        dialog.dismiss();
                    }
                });
                // Dialog showing
                dialog.show();
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);
    }
}
