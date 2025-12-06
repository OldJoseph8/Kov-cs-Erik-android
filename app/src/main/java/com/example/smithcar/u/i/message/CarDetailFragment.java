package com.example.smithcar.u.i;

import android.app.AlertDialog; // ÚJ IMPORT
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText; // ÚJ IMPORT
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smithcar.R;
// Győződj meg róla, hogy ez az adapter ebben a csomagban van:
import com.example.smithcar.u.i.ImageSliderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class CarDetailFragment extends Fragment {

    // Tároljuk az elérhetőséget, hogy a gombnyomáskor használhassuk
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

        // Lapozó elemek megkeresése
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

            // Leírás kezelése (ha nincs megadva, alapértelmezett szöveg)
            String desc = getArguments().getString("productDescription");
            if (desc != null && !desc.isEmpty()) {
                descriptionText.setText(desc);
            } else {
                descriptionText.setText("Nincs elérhető leírás.");
            }

            // Elérhetőség mentése (ha nincs megadva, alapértelmezett szám)
            String contact = getArguments().getString("productContact");
            if (contact != null && !contact.isEmpty()) {
                contactInfo = contact;
            } else {
                contactInfo = "+36 12 345 6789";
            }

            // Képek listájának átvétele
            ArrayList<String> argsImages = getArguments().getStringArrayList("productImages");
            if (argsImages != null && !argsImages.isEmpty()) {
                images.addAll(argsImages);
            }
        }

        // Ha véletlenül üres lenne a képlista, teszünk bele egy placeholdert
        if (images.isEmpty()) {
            images.add("https://via.placeholder.com/150"); // Alapértelmezett kép
        }

        // Adapter beállítása a képekhez
        // (Fontos: getContext() használata)
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(getContext(), images);
        viewPager.setAdapter(sliderAdapter);

        // Pöttyök összekötése a lapozóval
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Ide nem kell szöveg, csak a pöttyök jelennek meg
        }).attach();

        // --- MÓDOSÍTOTT KAPCSOLAT GOMB ---
        // Most már nem csak tárcsáz, hanem felhozza az ablakot az üzenetküldéssel
        btnContact.setOnClickListener(v -> {
            showContactDialog();
        });

        return view;
    }

    private void showContactDialog() {
        try {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // Itt használjuk a korábban létrehozott dialog_contact.xml-t
            View dialogView = inflater.inflate(R.layout.dialog_contact, null);

            TextView tvPhone = dialogView.findViewById(R.id.tvPhoneNumber);
            EditText etMessage = dialogView.findViewById(R.id.etMessage);

            if (tvPhone != null) {
                tvPhone.setText(contactInfo);
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("Kapcsolatfelvétel")
                    .setView(dialogView)

                    // GOMB 1: Hívás
                    .setPositiveButton("Hívás", (dialog, which) -> {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + contactInfo));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Hiba a hívásnál", Toast.LENGTH_SHORT).show();
                        }
                    })

                    // GOMB 2: Üzenet küldése (csak szimulálva)
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

                    // GOMB 3: Mégse
                    .setNegativeButton("Mégse", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}