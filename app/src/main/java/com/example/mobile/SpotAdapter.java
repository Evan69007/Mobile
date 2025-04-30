package com.example.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.Spot;

import java.util.List;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

    private List<Spot> spotList;
    public SpotAdapter(List<Spot> spotList) {
        this.spotList = spotList;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_spot, parent, false);
        return new SpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
        Spot spot = spotList.get(position);
        holder.spotName.setText(spot.getName());
        holder.spotLocation.setText(spot.getLocation());
        Picasso.get()
                .load(spot.getImage())
                .into(holder.spotImage);

        holder.spotImage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", spot.getName());
            bundle.putString("location", spot.getLocation());
            bundle.putString("image", spot.getImage());

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
        TextView spotName, spotLocation;

        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            spotImage = itemView.findViewById(R.id.imageView);
            spotName = itemView.findViewById(R.id.spotName);
            spotLocation = itemView.findViewById(R.id.Location);
        }
    }
}
