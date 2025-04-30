package com.example.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ListSpotBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
        try {
            JSONObject data = new JSONObject(loadJSONFromAsset());
            JSONArray records = data.getJSONArray("records");

            for (int i = 0; i < records.length(); i++) {
                JSONObject obj = records.getJSONObject(i);
                String name = obj.getString("Surf Break");
                String location = obj.getString("Address");
                String image = obj.getString("Photos");

                spots.add(new Spot(name, location, R.drawable.ic_launcher_foreground));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        SpotAdapter adapter = new SpotAdapter(spots);
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
