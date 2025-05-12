package com.example.mobile;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @GET(".") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getSurfSpots();


    @POST(".") // Replace with your Airtable API endpoint path
    Call<ApiResponse> AddSurfSpots(@Body RequestBody body);
}

