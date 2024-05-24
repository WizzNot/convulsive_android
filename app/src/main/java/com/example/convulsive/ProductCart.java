package com.example.convulsive;

import android.util.Log;

import java.util.Objects;

public class ProductCart{
    private String size;
    private String name;
    private int price;
    private int imageResource;
    public ProductCart(String name, int price, int imageResource, String size) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductCart)) return false;
        ProductCart that = (ProductCart) o;
        return price == that.price && imageResource == that.imageResource && Objects.equals(size, that.size) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, name, price, imageResource);
    }

    public String getName() {return name;}
    public int getPrice() {return price;}
    public int getImageResource() {return imageResource;}


    public String getSize() {return size;}
}
