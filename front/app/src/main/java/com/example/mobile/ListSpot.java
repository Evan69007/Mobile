package com.example.mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ListSpotBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSpot extends Fragment {
    private ListSpotBinding binding;
    private SpotAdapter adapter;
    private final List<Spot> spots = new ArrayList<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ListSpotBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize adapter with empty list
        adapter = new SpotAdapter(spots);
        recyclerView.setAdapter(adapter);

        // Fetch data
        SurfSpotRepository repository = new SurfSpotRepository();
        repository.fetchSurfSpots(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Record> records = response.body().records;
                    for (Record obj : records) {
                        Fields field = obj.fields;
                        float rating =field.rating;
                        String id = obj.id;
                        String name = field.Destination;
                        String location = field.DestinationStateCountry;
                        int difficulty = field.DifficultyLevel;
                        String peak_end = field.PeakSurfSeasonEnds;
                        String peak_start = field.PeakSurfSeasonBegins;
                        String image = (field.Photos != null && !field.Photos.isEmpty())
                                ? field.Photos.get(0).url
                                : null;
                        spots.add(new Spot(name, location, image, difficulty, peak_end, peak_start, id, rating));
                        adapter.notifyItemChanged(0); // ✅ Notify adapter here
                    }
                } else {
                    Log.e("API", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

