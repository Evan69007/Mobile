package com.example.mobile;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

    private List<Spot> spotList;
    public SpotAdapter(List<Spot> spotList) {
        this.spotList = spotList;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_layout, parent, false);
        return new SpotViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
        Spot spot = spotList.get(position);
        holder.spotName.setText(spot.getName());
        if (Objects.equals(spot.getName(), "No spots matches the filters"))
        {
            holder.add.setVisibility(INVISIBLE);
        }else {
            holder.add.setVisibility(VISIBLE);
        }
        holder.rating.setText(String.valueOf(spot.getRating()) + "⭐");
        holder.add.setOnClickListener(v-> {
            EditText input = new EditText(v.getContext());

            // Afficher le AlertDialog avec champ
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Add A rating")
                    .setView(input)
                    .setPositiveButton("Submit", (dialog, which) -> {
                        String rating = input.getText().toString();

                        Float ratingValue = Float.parseFloat(rating);
                        if (ratingValue < 0.0f || ratingValue > 5.0f)
                        {
                            return;
                        }
                        JSONObject data = new JSONObject();

                        try {
                            data.put("id", spot.getId());
                            data.put("rating", ratingValue);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        RequestBody requestBody = RequestBody.create(
                                MediaType.parse("application/json"),
                                data.toString()
                        );

                        // Appel au repository
                        SurfSpotRepository repository = new SurfSpotRepository();
                        repository.AddRating(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.i("API", "Réponse réussie : " + response.code());

                                } else {
                                    Log.e("API", "Réponse échouée : " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.e("API", "Erreur : " + t.getMessage());
                            }
                        }, requestBody);

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
//        holder.spotLocation.setText(spot.getLocation());
        Picasso.get()
                .load(spot.getImage())
                .into(holder.spotImage);

        holder.spotImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putFloat("rating",spot.getRating());
            bundle.putString("name", spot.getName());
            bundle.putString("image", spot.getImage());
            bundle.putString("id", spot.getId());

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.detail_spot, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return spotList.size();
    }

    public static class SpotViewHolder extends RecyclerView.ViewHolder {
        ImageView spotImage;
        TextView spotName, rating;
//        TextView spotLocation;
        Button add;

        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            spotImage = itemView.findViewById(R.id.imageView);
            spotName = itemView.findViewById(R.id.spotName);
            rating = itemView.findViewById(R.id.rating);
            add = itemView.findViewById(R.id.add);
//            spotLocation = itemView.findViewById(R.id.Location);
        }
    }
}
