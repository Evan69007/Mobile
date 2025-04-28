package com.example.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        // Lier les vues
        //spotImage = findViewById(R.id.spotImage);
        binding.spotName.setText("Plage de Bondi");
        // spotLocation = findViewById(R.id.spotLocation);

        // Mettre des valeurs
        //spotLocation.setText("Sydney, Australie");
        // spotImage.setImageResource(R.ic_launcher_background);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonNext.setOnClickListener(v ->
                NavHostFragment.findNavController(detail_spot.this)
                        .navigate(R.id.action_detail_to_SecondFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
