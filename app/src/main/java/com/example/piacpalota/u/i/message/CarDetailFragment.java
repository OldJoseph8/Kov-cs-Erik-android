package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;

public class CarDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_detail, container, false);

        // Elemek megkeresése
        TextView nameText = view.findViewById(R.id.detailName);
        TextView priceText = view.findViewById(R.id.detailPrice);
        TextView locationText = view.findViewById(R.id.detailLocation);
        TextView quantityText = view.findViewById(R.id.detailQuantity);
        ImageView imageView = view.findViewById(R.id.detailImage);
        View btnContact = view.findViewById(R.id.btnContact);

        // Adatok kinyerése a Bundle-ből (amit a BuyFragment küldött)
        if (getArguments() != null) {
            nameText.setText(getArguments().getString("productName"));
            priceText.setText(getArguments().getString("productPrice"));
            locationText.setText(getArguments().getString("productLocation"));
            quantityText.setText(getArguments().getString("productQuantity"));

            String imageUrl = getArguments().getString("productImageUrl");
            Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder).into(imageView);
        }

        // Gomb működése
        btnContact.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Hívás indítása...", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}