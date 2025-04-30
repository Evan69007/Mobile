package com.example.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mobile.databinding.MainPageBinding;
import com.squareup.picasso.Picasso;

public class main_page extends Fragment {

    private MainPageBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = MainPageBinding.inflate(inflater, container, false);

        ImageView backgroundImage = binding.backgroundImage;

        // Load the image from the URL using Picasso
        Picasso.get()
                .load("https://w0.peakpx.com/wallpaper/858/711/HD-wallpaper-wave-surfing-ocean-scenery-surf-summer.jpg") // Replace with your image URL
                .into(backgroundImage);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.logoImage.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.ListSpot);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}