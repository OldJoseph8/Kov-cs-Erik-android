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
import java.util.List;

public class SalesFragment extends Fragment {

    private ImageView ivSelectedImage;
    private List<String> selectedImageUris = new ArrayList<>(); // Lista a képeknek

    // --- TÖBB KÉP VÁLASZTÓ ---
    // A (5) jelenti a maximum darabszámot
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    // Ha választott képeket
                    selectedImageUris.clear(); // Előző választás törlése

                    // Átkonvertáljuk a kapott Uri-kat String-gé a tároláshoz
                    for (Uri uri : uris) {
                        selectedImageUris.add(uri.toString());
                    }

                    // Visszajelzés a felhasználónak
                    Toast.makeText(getContext(), uris.size() + " kép kiválasztva!", Toast.LENGTH_SHORT).show();

                    // Megjelenítjük az ELSŐ képet borítónak
                    Glide.with(this)
                            .load(uris.get(0))
                            .into(ivSelectedImage);
                } else {
                    // Ha visszalépett választás nélkül
                    Toast.makeText(getContext(), "Nem választottál képet.", Toast.LENGTH_SHORT).show();
                }
            });
    // -------------------------

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
            // Itt indítjuk a többes választót
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
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

            // Ha nem választott képet, teszünk be egy placeholdert
            if (selectedImageUris.isEmpty()) {
                selectedImageUris.add("https://via.placeholder.com/150");
            }

            // Mivel a selectedImageUris már lista, egyből átadhatjuk
            // De készítünk róla egy másolatot, hogy biztonságos legyen
            List<String> imagesToSave = new ArrayList<>(selectedImageUris);

            // Termék létrehozása és mentése (most már listát adunk át)
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