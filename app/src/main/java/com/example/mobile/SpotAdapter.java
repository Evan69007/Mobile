package com.example.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {
    private final List<Spot> spotList;

    public SpotAdapter(List<Spot> spotList) {
        this.spotList = spotList;
    }

    public static class SpotViewHolder extends RecyclerView.ViewHolder {
        ImageView spotImage;
        TextView spotName;
        TextView spotLocation;

        public SpotViewHolder(View itemView) {
            super(itemView);
            spotImage = itemView.findViewById(R.id.imageView);
            spotName = itemView.findViewById(R.id.spotName);
            spotLocation = itemView.findViewById(R.id.Location);
        }
    }

    @Override
    public SpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_spot, parent, false);
        return new SpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpotViewHolder holder, int position) {
        Spot spot = spotList.get(position);
        holder.spotName.setText(spot.getName());
        holder.spotLocation.setText(spot.getLocation());
        holder.spotImage.setImageResource(spot.getImage());
    }

    @Override
    public int getItemCount() {
        return spotList.size();
    }
}
