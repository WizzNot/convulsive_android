package com.example.convulsive.server_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Server {
    @POST("checkout")
    Call<CheckoutData> checkout(@Body CheckoutData requestData);
    @POST("contact")
    Call<CheckoutData> contact(@Body CheckoutData contactData);
}