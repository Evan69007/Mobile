package com.example.mobile;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mobile.databinding.DetailSpotBinding;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detail_spot extends Fragment {

    private DetailSpotBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DetailSpotBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        String id = args.getString("id");
        final String[] peak_start = {null};
        final String[] peak_end = {null};
        final String[] state_country = {null};
        final Integer[] difficulty = {null};

        SurfSpotRepository repository = new SurfSpotRepository();
        repository.fetchSpotDetail(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Record> records = response.body().records;
                    for (Record obj : records) {
                        Fields field = obj.fields;
                        difficulty[0] = field.DifficultyLevel;
                        peak_start[0] = field.PeakSurfSeasonBegins;
                        peak_end[0] = field.PeakSurfSeasonEnds;
                        state_country[0] = field.DestinationStateCountry;
                        binding.spotName.setText(args.getString("name"));
                        binding.level.setText(difficulty[0].toString());
                        binding.Location.setText(state_country[0]);
                        String peakStartFormatted = DateFormatter.reformatDate(peak_start[0]);
                        String peakEndFormatted = DateFormatter.reformatDate(peak_end[0]);
                        String fullPeak = peakStartFormatted + " to " + peakEndFormatted;
                        binding.peak.setText(fullPeak);
                        Picasso.get()
                                .load(args.getString("image"))
                                .into(binding.imageView);
                    }
                } else {
                    Log.e("API", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        }, id);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
