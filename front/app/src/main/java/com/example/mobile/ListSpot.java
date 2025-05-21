package com.example.mobile;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ListSpotBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        SurfSpotRepository repository = new SurfSpotRepository();

        Spinner ratingSpinner = binding.rating;
        String[] rating = {"rating", "1-5", "2-5", "3-5", "4-5", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                rating
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter);

        Spinner difficultySpinner = binding.difficulty;
        String[] difficulty = {"difficulty", "1", "2", "3", "4", "5"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                difficulty
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

/// ////////////////////////////////////////////////////////////////////
        Spinner destinationSpinner = binding.destination;
        List<String> destination = new ArrayList<>();
        destination.add("Destination");
        repository.fetchDestination(new Callback<ApiResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Record> records = response.body().records;
                    if (records != null && !records.isEmpty()) {
                        for (Record obj: records)
                        {
                            destination.add(obj.fields.Destination);
                        }
                        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(
                                requireContext(),
                                R.layout.spinner_item,
                                destination
                        );
                        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        destinationSpinner.setAdapter(destinationAdapter);
                    }

                } else {
                    Log.e("API", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
    });
 /////////////////////////////////////////////////////////////////////////////////

        AtomicInteger offset = new AtomicInteger();
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize adapter with empty list
        SpotAdapter spotAdapter = new SpotAdapter(spots);
        recyclerView.setAdapter(spotAdapter);



        binding.apply.setOnClickListener(v -> {
            Map<String, String> queryParams = new HashMap<>();

            int ratingPosition = ratingSpinner.getSelectedItemPosition();
            int difficultyPosition = difficultySpinner.getSelectedItemPosition();
            int destinationPosition = destinationSpinner.getSelectedItemPosition();
            if(ratingPosition == 0 && difficultyPosition == 0 && destinationPosition == 0){
                return;
            }
            if (difficultyPosition != 0)
            {
                queryParams.put("difficulty", difficultySpinner.getSelectedItem().toString());
            }
            if(destinationPosition != 0){
                queryParams.put("destination", destinationSpinner.getSelectedItem().toString());
            }
            if(ratingPosition == 1){
                queryParams.put("rating", "1");
            }
            if(ratingPosition == 2){
                queryParams.put("rating", "2");
            }
            if(ratingPosition == 3) {
                queryParams.put("rating", "3");
            }
            if(ratingPosition == 4) {
                queryParams.put("rating", "4");
            }
            if(ratingPosition == 5) {
                queryParams.put("rating", "5");
            }
            repository.fetchfilteredSpots(new Callback<ApiResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Record> records = response.body().records;
                        if (records != null && !records.isEmpty()) {
                            spots.clear();
                            for (Record obj : records) {
                                Fields field = obj.fields;
                                float rating = field.rating;
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
                            }
                            spotAdapter.notifyDataSetChanged();
                            binding.more.setVisibility(INVISIBLE);
                        }
                        else
                        {
                            spots.clear();
                            String name = "No spots matches the filters";
                            spots.add(new Spot(name, "", "no image", 0, "", "", "", 0));
                            spotAdapter.notifyDataSetChanged();
                            binding.more.setVisibility(INVISIBLE);
                        }
                    } else {
                        Log.e("API", "Response failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    Log.e("API", "Error: " + t.getMessage());
                }
            }, queryParams);
        });
        // Fetch data

        repository.fetchSurfSpots(new Callback<ApiResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Record> records = response.body().records;
                    for (Record obj : records) {
                        Fields field = obj.fields;
                        float rating = field.rating;
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
                    }
                    offset.addAndGet(3);
                    spotAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        }, offset.get());
        binding.more.setOnClickListener(v -> {
            repository.fetchSurfSpots(new Callback<ApiResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Record> records = response.body().records;
                        if (records != null)
                        {
                            for (Record obj : records) {
                                Fields field = obj.fields;
                                float rating = field.rating;
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
                            }
                            offset.addAndGet(3);
                            spotAdapter.notifyDataSetChanged();
                            if (records.size() < 3)
                            {
                                binding.more.setVisibility(INVISIBLE);
                            }
                        }else{
                            binding.more.setVisibility(INVISIBLE);
                        }
                    } else {
                        Log.e("API", "Response failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("API", "Error: " + t.getMessage());
                }
            }, offset.get());
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

