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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ListSpotBinding;

import java.util.ArrayList;
import java.util.List;

public class ListSpot extends Fragment {
    private ListSpotBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ListSpotBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot("Plage des Dames", "Noirmoutier, France", R.drawable.ic_launcher_foreground));
        spots.add(new Spot("Dune du Pilat", "Arcachon, France", R.drawable.ic_launcher_foreground));

        SpotAdapter adapter = new SpotAdapter(spots);
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setOnClickListener(v ->
                NavHostFragment.findNavController(ListSpot.this)
                        .navigate(R.id.action_list_to_detail)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
