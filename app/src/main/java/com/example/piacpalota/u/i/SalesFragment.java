package com.example.piacpalota.u.i;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.CarRepository;
import com.example.piacpalota.u.i.buylist.Product;

import java.util.ArrayList;
import java.util.List;

public class SalesFragment extends Fragment {

    private List<String> selectedImageUris = new ArrayList<>();
    private TextView txtImageCount;

    // Galéria megnyitó
    private final ActivityResultLauncher<Intent> pickImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUris.clear(); // Töröljük az előző választást
                    Intent data = result.getData();

                    // Ha több képet választott
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        if (count > 5) {
                            Toast.makeText(getContext(), "Maximum 5 képet tölthetsz fel! Az első 5 került kiválasztásra.", Toast.LENGTH_LONG).show();
                            count = 5;
                        }
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            selectedImageUris.add(imageUri.toString());
                        }
                    }
                    // Ha csak egy képet választott
                    else if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        selectedImageUris.add(imageUri.toString());
                    }

                    updateImageCountText();
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        EditText inputName = view.findViewById(R.id.inputName);
        EditText inputPrice = view.findViewById(R.id.inputPrice);
        EditText inputLocation = view.findViewById(R.id.inputLocation);
        Button btnSelectImages = view.findViewById(R.id.btnSelectImages);
        txtImageCount = view.findViewById(R.id.txtImageCount);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        // Képválasztó gomb
        btnSelectImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Több kép engedélyezése
            pickImagesLauncher.launch(Intent.createChooser(intent, "Válassz képeket"));
        });

        // Beküldés gomb
        btnSubmit.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String price = inputPrice.getText().toString();
            String location = inputLocation.getText().toString();

            if (name.isEmpty() || price.isEmpty() || location.isEmpty()) {
                Toast.makeText(getContext(), "Kérlek tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUris.isEmpty()) {
                // Ha nem választott képet, teszünk be egy placeholdert
                selectedImageUris.add("https://via.placeholder.com/150");
            }

            // Új listát hozunk létre a tároláshoz, hogy ne vesszen el a referencia
            List<String> imagesToSave = new ArrayList<>(selectedImageUris);

            Product newCar = new Product(name, price, "1 db", location, imagesToSave);
            CarRepository.getInstance().addProduct(newCar);

            Toast.makeText(getContext(), "Hirdetés sikeresen feladva!", Toast.LENGTH_SHORT).show();

            try {
                Navigation.findNavController(view).navigate(R.id.buyFragment);
            } catch (Exception e) {
                // Hiba kezelés
            }
        });

        return view;
    }

    private void updateImageCountText() {
        if (selectedImageUris.isEmpty()) {
            txtImageCount.setText("Nincs kép kiválasztva");
        } else {
            txtImageCount.setText(selectedImageUris.size() + " kép kiválasztva");
        }
    }
}