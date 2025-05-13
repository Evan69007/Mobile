package com.example.mobile;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
//                            .addHeader("Authorization", "Bearer patLinxDfQmD3pA8h.031e67116f7078e1000628fe693c735cba92524421ae7f12749e28f97e751ce2")
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.19.75.76:8080/api/") // Include trailing slash
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void fetchSurfSpots(Callback<ApiResponse> callback) {
        Call<ApiResponse> call = apiService.getSurfSpots();
        call.enqueue(callback);
    }

    public void fetchSpotDetail(Callback<ApiResponse> callback, String id) {
        Call<ApiResponse> call = apiService.getSurfDetail(id);
        call.enqueue(callback);
    }

    public void AddSurfSpot(Callback<ApiResponse> callback, RequestBody body){
        Call<ApiResponse> call = apiService.AddSurfSpots(body);
        call.enqueue(callback);
    }
}