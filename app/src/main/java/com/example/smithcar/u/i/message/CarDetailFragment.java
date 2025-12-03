package com.example.smithcar.u.i;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smithcar.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class CarDetailFragment extends Fragment {

    // Elérhetőség tárolása
    private String contactInfo = "";

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

        // Lapozó elemek
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerImages);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutIndicator);

        // Képek listája
        ArrayList<String> images = new ArrayList<>();

        // Adatok fogadása a Bundle-ből
        if (getArguments() != null) {
            nameText.setText(getArguments().getString("productName"));
            priceText.setText(getArguments().getString("productPrice"));
            locationText.setText(getArguments().getString("productLocation"));
            quantityText.setText(getArguments().getString("productQuantity"));

            // Leírás kezelése
            String desc = getArguments().getString("productDescription");
            if (desc != null && !desc.isEmpty()) {
                descriptionText.setText(desc);
            } else {
                descriptionText.setText("Nincs elérhető leírás.");
            }

            // Elérhetőség elmentése a gombhoz
            String contact = getArguments().getString("productContact");
            if (contact != null && !contact.isEmpty()) {
                contactInfo = contact;
            } else {
                contactInfo = "+36 12 345 6789"; // Alapértelmezett szám, ha nincs megadva
            }

            // Képek listájának átvétele
            ArrayList<String> argsImages = getArguments().getStringArrayList("productImages");
            if (argsImages != null && !argsImages.isEmpty()) {
                images.addAll(argsImages);
            }
        }

        // Ha véletlenül üres lenne a képlista, teszünk bele egy placeholdert
        if (images.isEmpty()) {
            images.add("https://via.placeholder.com/400");
        }

        // Adapter beállítása
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), images);
        viewPager.setAdapter(sliderAdapter);

        // Pöttyök összekötése a lapozóval
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Ide nem kell szöveg
        }).attach();

        // Kapcsolat gomb - Tárcsázó megnyitása
        btnContact.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactInfo));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Elérhetőség: " + contactInfo, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}