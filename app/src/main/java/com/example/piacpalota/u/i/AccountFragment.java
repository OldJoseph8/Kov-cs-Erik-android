package com.example.piacpalota.u.i;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;

public class AccountFragment extends Fragment {

    private ImageView profileImageView;
    private EditText nameEditText;
    private Button uploadButton;
    private Button updateNameButton;

    // Helyi adattároló (SharedPreferences)
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserProfile";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_PROFILE_IMAGE = "profileImage";

    // Modern képválasztó
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    // 1. Megjelenítjük a képet
                    Glide.with(this)
                            .load(uri)
                            .circleCrop() // Kerek profilkép
                            .into(profileImageView);

                    // 2. Elmentjük a kép címét
                    sharedPreferences.edit().putString(KEY_PROFILE_IMAGE, uri.toString()).apply();

                    Toast.makeText(getContext(), "Profilkép frissítve!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Adattároló inicializálása
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // UI elemek
        profileImageView = view.findViewById(R.id.profileImageView);
        nameEditText = view.findViewById(R.id.nameEditText);
        uploadButton = view.findViewById(R.id.uploadButton);
        updateNameButton = view.findViewById(R.id.updateNameButton);

        // --- ADATOK BETÖLTÉSE ---
        loadUserData();

        // --- GOMBOK KEZELÉSE ---

        // 1. Képfeltöltés gomb
        uploadButton.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        // 2. Név frissítése gomb
        updateNameButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString().trim();
            if (!newName.isEmpty()) {
                // Mentés a memóriába
                sharedPreferences.edit().putString(KEY_USER_NAME, newName).apply();
                Toast.makeText(getContext(), "Név sikeresen mentve!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "A név nem lehet üres!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadUserData() {
        // Név betöltése (Alapértelmezett: "Felhasználó")
        String savedName = sharedPreferences.getString(KEY_USER_NAME, "SmithCar Felhasználó");
        nameEditText.setText(savedName);

        // Kép betöltése
        String savedImageUri = sharedPreferences.getString(KEY_PROFILE_IMAGE, null);
        if (savedImageUri != null) {
            Glide.with(this)
                    .load(Uri.parse(savedImageUri))
                    .circleCrop()
                    .placeholder(R.drawable.placeholder) // JAVÍTVA: account -> placeholder
                    .into(profileImageView);
        } else {
            // Ha nincs mentett kép, az alapértelmezettet mutatjuk
            Glide.with(this)
                    .load(R.drawable.placeholder) // JAVÍTVA: account -> placeholder
                    .circleCrop()
                    .into(profileImageView);
        }
    }
}