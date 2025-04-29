package com.example.mobile;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mobile.databinding.DetailSpotBinding;

public class detail_spot extends Fragment {

    private DetailSpotBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DetailSpotBinding.inflate(inflater, container, false);

        Bundle args = getArguments();
        if (args != null) {
            binding.spotName.setText(args.getString("name"));
            binding.Location.setText(args.getString("location"));
            Drawable drawable = ContextCompat.getDrawable(requireContext(), args.getInt("image"));
            binding.imageView.setImageDrawable(drawable);
        }
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
