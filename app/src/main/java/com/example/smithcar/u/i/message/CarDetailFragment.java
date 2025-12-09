package com.example.smithcar.u.i;

import android.app.AlertDialog; // Dialoghoz
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Gomb
import android.widget.EditText; // Szövegmező a dialogban
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smithcar.R;
import com.example.smithcar.u.i.ImageSliderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class CarDetailFragment extends Fragment {

    // Tároljuk az elérhetőséget
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

        // "Érdeklődöm" / Kapcsolat gomb
        Button btnContact = view.findViewById(R.id.btnContact);

        // Lapozó elemek
        ViewPager2 viewPager = view.findViewById(R.id.viewPagerImages);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutIndicator);

        ArrayList<String> images = new ArrayList<>();

        // Adatok fogadása
        if (getArguments() != null) {
            nameText.setText(getArguments().getString("productName"));
            priceText.setText(getArguments().getString("productPrice"));
            locationText.setText(getArguments().getString("productLocation"));
            quantityText.setText(getArguments().getString("productQuantity"));

            String desc = getArguments().getString("productDescription");
            if (desc != null && !desc.isEmpty()) {
                descriptionText.setText(desc);
            } else {
                descriptionText.setText("Nincs elérhető leírás.");
            }

            // Elérhetőség mentése
            String contact = getArguments().getString("productContact");
            if (contact != null && !contact.isEmpty()) {
                contactInfo = contact;
            } else {
                contactInfo = "+36 12 345 6789";
            }

            ArrayList<String> argsImages = getArguments().getStringArrayList("productImages");
            if (argsImages != null && !argsImages.isEmpty()) {
                images.addAll(argsImages);
            }
        }

        if (images.isEmpty()) {
            images.add("https://via.placeholder.com/400");
        }

        // Adapter beállítása
        if (getContext() != null) {
            ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), images);
            viewPager.setAdapter(sliderAdapter);

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();
        }

        // --- GOMB MŰKÖDÉSE ---
        // Most már nem a tárcsázót nyitja, hanem a Dialogot!
        btnContact.setOnClickListener(v -> {
            showContactDialog();
        });

        return view;
    }

    // --- A KAPCSOLAT ABLAK MEGJELENÍTÉSE ---
    private void showContactDialog() {
        try {
            // 1. Betöltjük az egyedi ablak kinézetét a dialog_contact.xml fájlból
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // Itt hivatkozunk a létrehozott dialog_contact.xml layoutra
            View dialogView = inflater.inflate(R.layout.dialog_contact, null);

            // 2. Elemek keresése az ablakon belül (dialog_contact.xml-ben lévő ID-k alapján)
            // Győződj meg róla, hogy ezek az ID-k léteznek a dialog_contact.xml fájlban!
            TextView tvPhone = dialogView.findViewById(R.id.tvPhoneNumber);
            EditText etMessage = dialogView.findViewById(R.id.etMessage);

            // 3. Szám beállítása
            if (tvPhone != null) {
                tvPhone.setText(contactInfo);
            }

            // 4. Ablak megjelenítése
            new AlertDialog.Builder(getContext())
                    .setTitle("Kapcsolatfelvétel")
                    .setView(dialogView) // Itt adjuk át az egyedi nézetet

                    // GOMB 1: HÍVÁS
                    .setPositiveButton("Hívás", (dialog, which) -> {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + contactInfo));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Nem sikerült a hívás.", Toast.LENGTH_SHORT).show();
                        }
                    })

                    // GOMB 2: ÜZENET KÜLDÉSE
                    .setNeutralButton("Küldés", (dialog, which) -> {
                        String message = "";
                        if (etMessage != null) {
                            message = etMessage.getText().toString();
                        }

                        if (!message.isEmpty()) {
                            Toast.makeText(getContext(), "Üzenet elküldve!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Nem írtál üzenetet!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    // GOMB 3: MÉGSE
                    .setNegativeButton("Mégse", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Hiba az ablak megnyitásakor: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}