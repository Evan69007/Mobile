package com.example.mobile;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    @GET("spots") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getSurfSpots(@Query("offset") int offset);

    @GET("spots/{id}") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getSurfDetail(@Path("id") String spotId);

    @GET("filteredspots")
    Call<ApiResponse> fetchSurfSpots(@QueryMap Map<String, String> options);

    @GET("destination") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getDestination();


    @POST("addspots") // Replace with your Airtable API endpoint path
    Call<ApiResponse> AddSurfSpots(@Body RequestBody body);

    @POST("spots") // Replace with your Airtable API endpoint path
    Call<ApiResponse> AddRating(@Body RequestBody body);

    @PUT("spots/{id}")
    Call<ApiResponse> EditRating(@Body RequestBody body,@Path("id") String spotId);


}

