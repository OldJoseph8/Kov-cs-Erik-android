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

    private String contactInfo = "+36 12 345 6789"; // Alapértelmezett szám

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // XML betöltése
        View view = inflater.inflate(R.layout.fragment_car_detail, container, false);

        // Elemek megkeresése
        TextView nameText = view.findViewById(R.id.detailName);
        TextView priceText = view.findViewById(R.id.detailPrice);
        TextView locationText = view.findViewById(R.id.detailLocation);
        TextView quantityText = view.findViewById(R.id.detailQuantity);
        TextView descriptionText = view.findViewById(R.id.tvDescription);
        View btnContact = view.findViewById(R.id.btnContact);

        ViewPager2 viewPager = view.findViewById(R.id.viewPagerImages);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutIndicator);

        // Lista inicializálása
        ArrayList<String> images = new ArrayList<>();

        // Adatok betöltése (Védett módban)
        if (getArguments() != null) {
            String name = getArguments().getString("productName");
            if (name != null) nameText.setText(name);
            else nameText.setText("Név nem található");

            String price = getArguments().getString("productPrice");
            if (price != null) priceText.setText(price);

            String loc = getArguments().getString("productLocation");
            if (loc != null) locationText.setText(loc);

            String quant = getArguments().getString("productQuantity");
            if (quant != null) quantityText.setText(quant);

            String desc = getArguments().getString("productDescription");
            if (desc != null && !desc.isEmpty()) {
                descriptionText.setText(desc);
            } else {
                descriptionText.setText("Nincs leírás.");
            }

            String contact = getArguments().getString("productContact");
            if (contact != null && !contact.isEmpty()) contactInfo = contact;

            ArrayList<String> argsImages = getArguments().getStringArrayList("productImages");
            if (argsImages != null) {
                images.addAll(argsImages);
            }
        } else {
            // Ha nincs argumentum, akkor is írjon ki valamit
            nameText.setText("HIBA: Nincs adatátadás!");
        }

        // Képkezelés (Ha üres a lista, tegyünk bele egy placeholdert)
        if (images.isEmpty()) {
            images.add("https://via.placeholder.com/400");
        }

        // Adapter beállítása (MINDIG lefut)
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), images);
        viewPager.setAdapter(sliderAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        // Gomb működése
        btnContact.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactInfo));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Szám: " + contactInfo, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}