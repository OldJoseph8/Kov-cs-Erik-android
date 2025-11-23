package com.example.piacpalota.u.i;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.CarRepository;
import com.example.piacpalota.u.i.buylist.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesFragment extends Fragment {

    private ImageView ivSelectedImage;
    private Uri selectedImageUri; // Itt tároljuk a kiválasztott képet

    // Egyszerű képválasztó
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    // Megjelenítjük a képet
                    Glide.with(this).load(uri).into(ivSelectedImage);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        EditText inputName = view.findViewById(R.id.inputName);
        EditText inputPrice = view.findViewById(R.id.inputPrice);
        EditText inputLocation = view.findViewById(R.id.inputLocation);
        EditText inputDescription = view.findViewById(R.id.inputDescription);

        ivSelectedImage = view.findViewById(R.id.ivSelectedImage);
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        // Képválasztás indítása
        btnSelectImage.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        // Beküldés
        btnSubmit.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String price = inputPrice.getText().toString();
            String location = inputLocation.getText().toString();
            String description = inputDescription.getText().toString();

            if (name.isEmpty() || price.isEmpty() || location.isEmpty()) {
                Toast.makeText(getContext(), "Kérlek tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Képlista előkészítése (ha nincs kép, placeholder megy bele)
            List<String> imagesToSave = new ArrayList<>();
            if (selectedImageUri != null) {
                imagesToSave.add(selectedImageUri.toString());
            } else {
                imagesToSave.add("https://via.placeholder.com/150");
            }

            // Termék létrehozása és mentése
            Product newCar = new Product(name, price, "1 db", location, description, imagesToSave);
            CarRepository.getInstance().addProduct(newCar);

            Toast.makeText(getContext(), "Hirdetés sikeresen feladva!", Toast.LENGTH_SHORT).show();

            // Visszalépés a listához
            try {
                Navigation.findNavController(view).navigate(R.id.buyFragment);
            } catch (Exception e) {
                // Hiba esetén maradjunk
            }
        });

        return view;
    }
}