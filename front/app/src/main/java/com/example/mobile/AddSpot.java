package com.example.mobile;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mobile.databinding.AddSpotBinding;
import com.example.mobile.databinding.ListSpotBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSpot extends Fragment {

    private AddSpotBinding binding;
    private Calendar calendar;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = AddSpotBinding.inflate(inflater, container, false);

        calendar = Calendar.getInstance();

        binding.peakBegin.setFocusable(false);
        binding.peakBegin.setClickable(true);

        binding.peakBegin.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedYear + "-" + ((selectedMonth + 1 > 9) ? "" : "0") + (selectedMonth + 1) + "-" + ((selectedDay + 1 > 9) ? "" : "0") + selectedDay;
                        binding.peakBegin.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        binding.peakEnd.setFocusable(false);
        binding.peakEnd.setClickable(true);

        binding.peakEnd.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedYear + "-" + ((selectedMonth + 1 > 9) ? "" : "0") + (selectedMonth + 1) + "-" + ((selectedDay + 1 > 9) ? "" : "0") + selectedDay;
                        binding.peakEnd.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        binding.Submit.setOnClickListener(v -> {
            try {
                JSONObject data = new JSONObject();

                data.put("Destination", binding.Destination.getText().toString());

                data.put("Destination State/Country", binding.DestinationStateCountry.getText().toString());

                data.put("Peak Surf Season Begins", binding.peakBegin.getText().toString());

                data.put("Peak Surf Season Ends", binding.peakEnd.getText().toString());

                int difficulty = Integer.parseInt(binding.difficulty.getText().toString());
                data.put("Difficulty Level", difficulty);

                data.put("Surf Break",  binding.SurfBreak.getText().toString());

                data.put("url", binding.url.getText().toString());
                RequestBody requestBody = RequestBody.create(
                        MediaType.parse("application/json"),
                        data.toString()
                );

                // Appel au repository
                SurfSpotRepository repository = new SurfSpotRepository();
                repository.AddSurfSpot(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.i("API", "Réponse réussie : " + response.code());
                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.ListSpot);

                        } else {
                            Log.e("API", "Réponse échouée : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.e("API", "Erreur : " + t.getMessage());
                    }
                }, requestBody);

            } catch (JSONException e) {
                Log.e("JSON", "Erreur de création du JSON : " + e.getMessage());
            }
        });

        return binding.getRoot();
    }
}
