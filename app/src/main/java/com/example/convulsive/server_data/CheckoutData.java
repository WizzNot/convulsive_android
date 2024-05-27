package com.example.convulsive.server_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutData {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phoneNumber;
    @SerializedName("items")
    @Expose
    private String items;
    @SerializedName("price")
    @Expose
    private int price;

    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getItems() {return items;}
    public int getPrice() {return price;}

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setItems(String items) {this.items = items;}
    public void setPrice(int price) {this.price = price;}

    public CheckoutData(String name, String email, String phoneNumber, String items, int price){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.price = price;
    }
    public CheckoutData(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
