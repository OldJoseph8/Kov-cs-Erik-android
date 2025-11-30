package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.piacpalota.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class CarDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_detail, container, false);

        // UI elemek megkeresése
        TextView nameText = view.findViewById(R.id.detailName);
        TextView priceText = view.findViewById(R.id.detailPrice);
        TextView locationText = view.findViewById(R.id.detailLocation);
        TextView quantityText = view.findViewById(R.id.detailQuantity);
        TextView descriptionText = view.findViewById(R.id.tvDescription);
        View btnContact = view.findViewById(R.id.btnContact);

        // Lapozó elemek megkeresése
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerImages);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutIndicator);

        // Adatok fogadása a Bundle-ből
        if (getArguments() != null) {
            nameText.setText(getArguments().getString("productName"));
            priceText.setText(getArguments().getString("productPrice"));
            locationText.setText(getArguments().getString("productLocation"));
            quantityText.setText(getArguments().getString("productQuantity"));
            descriptionText.setText(getArguments().getString("productDescription"));

            // Képek listájának átvétele
            ArrayList<String> images = getArguments().getStringArrayList("productImages");

            // Ha véletlenül üres lenne a képlista, teszünk bele egy placeholdert
            if (images == null || images.isEmpty()) {
                images = new ArrayList<>();
                images.add("https://via.placeholder.com/400"); // Alapértelmezett kép
            }

            // Képlapozó beállítása az Adapterrel
            ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), images);
            viewPager.setAdapter(sliderAdapter);

            // Pöttyök összekötése a lapozóval
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                // Ide nem kell szöveg, csak a pöttyök jelennek meg
            }).attach();
        }

        // Kapcsolat gomb
        btnContact.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Hívás indítása...", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}