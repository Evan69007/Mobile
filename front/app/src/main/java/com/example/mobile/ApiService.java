package com.example.mobile;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("spots") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getSurfSpots();

    @GET("spots/{id}") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getSurfDetail(@Path("id") String spotId);

    @POST("spots") // Replace with your Airtable API endpoint path
    Call<ApiResponse> AddSurfSpots(@Body RequestBody body);
}

