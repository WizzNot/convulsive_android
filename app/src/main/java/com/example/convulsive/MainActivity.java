package com.example.convulsive;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.security.AccessController.getContext;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private HashMap<ProductCart, Integer> cart = new HashMap<>();
    private boolean cart_is_visible = false;

    // В MainActivity
    private void populateCart(Dialog dialog) {
        LinearLayout cartItemContainer = dialog.findViewById(R.id.cart_item_container);

        // Удаляем все существующие карточки товаров
        cartItemContainer.removeAllViews();

        for (Map.Entry<ProductCart, Integer> entry : cart.entrySet()) {
            ProductCart productCart = entry.getKey();
            int quantity = entry.getValue();

            // Создаем карточку товара
            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item_product, null);

            // Заполняем данные карточки товара
            ImageView itemImage = cartItemView.findViewById(R.id.cart_itemimage);
            itemImage.setImageResource(productCart.getImageResource());

            TextView itemName = cartItemView.findViewById(R.id.cart_itemname);
            itemName.setText(productCart.getName());

            TextView itemPrice = cartItemView.findViewById(R.id.cart_itemprice);
            itemPrice.setText(String.valueOf(productCart.getPrice() * quantity) + "Р");

            TextView itemCount = cartItemView.findViewById(R.id.cart_countofitem);
            itemCount.setText(String.valueOf(quantity));

            TextView itemSize = cartItemView.findViewById(R.id.cart_itemsize);
            itemSize.setText(productCart.getSize());

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
        ImageButton cartButton = findViewById(R.id.cartButton);
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
                        Log.d("contact", (String)name.getText().toString() + " " + (String)phone.getText().toString());
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