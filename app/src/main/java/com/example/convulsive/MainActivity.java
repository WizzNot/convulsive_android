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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private HashMap<Product, Integer> cart = new HashMap<>();
    private boolean cart_is_visible = false;


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
        // cart making
        for(Product x:productList) {
            cart.put(x, 0);
        }
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
                        cart.put(product, cart.get(product) + 1);
                        if(!cart_is_visible) {
                            cart_is_visible = true;
                            cartButton.setVisibility(View.VISIBLE);
                        }
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