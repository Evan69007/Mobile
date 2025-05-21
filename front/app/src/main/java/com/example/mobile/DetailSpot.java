package com.example.mobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.databinding.DetailSpotBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSpot extends Fragment {

    private DetailSpotBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DetailSpotBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        String id = args.getString("id");

        SurfSpotRepository repository = new SurfSpotRepository();
        repository.fetchSpotDetail(new Callback<ApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Record> records = response.body().records;
                    for (Record obj : records) {
                        Fields field = obj.fields;
                        int difficulty = field.DifficultyLevel;
                        String peak_start = field.PeakSurfSeasonBegins;
                        String peak_end = field.PeakSurfSeasonEnds;
                        String state_country = field.DestinationStateCountry;
                        binding.spotName.setText(args.getString("name"));
                        binding.level.setText(String.valueOf(difficulty));
                        binding.Location.setText(state_country);
                        binding.rating.setText(String.valueOf(args.getFloat("rating")) + "⭐");
                        String peakStartFormatted = DateFormatter.reformatDate(peak_start);
                        String peakEndFormatted = DateFormatter.reformatDate(peak_end);
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

        binding.edit.setOnClickListener(v -> {
            binding.rating.setEnabled(true);
            binding.rating.setFocusable(true);
            binding.rating.setFocusableInTouchMode(true);
        });

        binding.rating.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Check if the action is "Enter" or "Done"
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    // Do your action here
                    String inputText = binding.rating.getText().toString();
                    Log.d("InputText", "User entered: " + inputText);

                    JSONObject data = new JSONObject();

                    try {
                        float ratingValue = Float.parseFloat(inputText);

                        if (ratingValue < 0.0f || ratingValue > 5.0f) {
                            Toast.makeText(v.getContext(), "La note doit être entre 0 et 5", Toast.LENGTH_SHORT).show();
                        } else {
                            data.put("rating", ratingValue);
                            // Ici tu peux continuer (par exemple : envoyer data à un serveur)
                            Log.d("JSON", "Data prête à l'envoi : " + data.toString());
                        }

                    } catch (NumberFormatException e) {
                        Log.e("ParseError", "Valeur invalide : " + inputText, e);
                        Toast.makeText(v.getContext(), "Veuillez entrer un nombre valide", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e("JSONError", "Erreur lors de la création du JSON", e);
                    }


                    RequestBody requestBody = RequestBody.create(
                            MediaType.parse("application/json"),
                            data.toString()
                    );

                    SurfSpotRepository repository = new SurfSpotRepository();
                    repository.EditRating(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                return;
                            } else {
                                Log.e("API", "Response failed: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Log.e("API", "Error: " + t.getMessage());
                        }
                    }, requestBody, id);
                }
                return false;
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
