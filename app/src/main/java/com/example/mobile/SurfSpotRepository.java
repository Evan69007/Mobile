package com.example.mobile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SurfSpotRepository {

    private ApiService apiService;

    public SurfSpotRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer patLinxDfQmD3pA8h.031e67116f7078e1000628fe693c735cba92524421ae7f12749e28f97e751ce2")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.airtable.com/v0/appsSzgNEnj6SuGw6/Surf%20Destinations/") // Include trailing slash
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void fetchSurfSpots(Callback<ApiResponse> callback) {
        Call<ApiResponse> call = apiService.getSurfSpots();
        call.enqueue(callback);
    }
}