

package com.example.smithcar.u.i;

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

import com.bumptech.glide.Glide;
import com.example.smithcar.MainActivity;
import com.example.smithcar.R;
import com.example.smithcar.u.i.buylist.CarRepository;
import com.example.smithcar.u.i.buylist.Product;

import java.util.ArrayList;
import java.util.List;

public class SalesFragment extends Fragment {

    private ImageView ivSelectedImage;
    private List<String> selectedImageUris = new ArrayList<>(); // Lista a képeknek

    // --- TÖBB KÉP VÁLASZTÓ (Max 5) ---
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    selectedImageUris.clear(); // Előző választás törlése

                    // Uri-k mentése String-ként
                    for (Uri uri : uris) {
                        selectedImageUris.add(uri.toString());
                    }

                    Toast.makeText(getContext(), uris.size() + " kép kiválasztva!", Toast.LENGTH_SHORT).show();

                    // Az első képet megjelenítjük borítónak
                    Glide.with(this)
                            .load(uris.get(0))
                            .into(ivSelectedImage);
                } else {
                    Toast.makeText(getContext(), "Nem választottál képet.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        // UI elemek megkeresése
        EditText inputName = view.findViewById(R.id.inputName);
        EditText inputPrice = view.findViewById(R.id.inputPrice);
        EditText inputLocation = view.findViewById(R.id.inputLocation);
        EditText inputDescription = view.findViewById(R.id.inputDescription);
        // Elérhetőség mező (opcionális, ha benne van az XML-ben)
        EditText inputContact = view.findViewById(R.id.inputContact);

        ivSelectedImage = view.findViewById(R.id.ivSelectedImage);
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        // Képválasztás indítása
        btnSelectImage.setOnClickListener(v -> {
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        // Beküldés gomb
        btnSubmit.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String price = inputPrice.getText().toString();
            String location = inputLocation.getText().toString();
            String description = inputDescription.getText().toString();

            String contact = "";
            if (inputContact != null) {
                contact = inputContact.getText().toString();
            }

            if (name.isEmpty() || price.isEmpty() || location.isEmpty()) {
                Toast.makeText(getContext(), "Kérlek tölts ki minden kötelező mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ha nem választott képet, teszünk be egy placeholdert
            if (selectedImageUris.isEmpty()) {
                selectedImageUris.add("https://via.placeholder.com/150");
            }

            // Képlista másolata a mentéshez
            List<String> imagesToSave = new ArrayList<>(selectedImageUris);

            // Termék létrehozása
            Product newCar = new Product(name, price, "1 db", location, description, contact, imagesToSave);

            // Mentés a közös tárolóba
            CarRepository.getInstance().addProduct(newCar);

            Toast.makeText(getContext(), "Hirdetés sikeresen feladva!", Toast.LENGTH_SHORT).show();

            // Visszalépés a listához (A MainActivity biztonságos navigateTo függvényével)
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(R.id.buyFragment, null);
            }
        });

        return view;
    }
}