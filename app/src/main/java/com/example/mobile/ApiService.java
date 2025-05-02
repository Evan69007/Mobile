package com.example.mobile;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("https://api.airtable.com/v0/appsSzgNEnj6SuGw6/Surf%20Destinations") // Replace with your Airtable API endpoint path
    Call<ApiResponse> getSurfSpots();
}

