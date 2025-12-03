package com.example.smithcar.u.i;

import android.os.Bundle;
// import android.util.Log; // TÖRÖLVE
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
// import android.widget.Toast; // TÖRÖLVE

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smithcar.R;
// --- FIREBASE IMPORT-ok TÖRÖLVE ---
// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeFragment extends Fragment {

    private TextView welcomeText;
    // --- FIREBASE VÁLTOZÓK TÖRÖLVE ---
    // private FirebaseAuth mAuth;
    // private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeText = view.findViewById(R.id.welcomeText);
        // --- FIREBASE INICIALIZÁLÁS TÖRÖLVE ---
        // mAuth = FirebaseAuth.getInstance();
        // db = FirebaseFirestore.getInstance();

        // --- FIREBASE NÉV LEKÉRDEZÉS TÖRÖLVE (már nincs rá szükség) ---
        // Csak beállítjuk az alapértelmezett üdvözlést
        welcomeText.setText("Üdvözlünk, Kedves Felhasználó!");


        // Gombok inicializálása
        Button buyButton = view.findViewById(R.id.welcBuyButton);
        Button saleButton = view.findViewById(R.id.welcSaleButton);

        // Vásárolok gomb eseménykezelése
        buyButton.setOnClickListener(v -> {
            // --- AZ IF / ELSE ELLENŐRZÉS TÖRÖLVE ---
            // A kód most már azonnal továbblép

            // FragmentTransaction használata a navigációhoz
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new BuyFragment());  // Replace a buyFragment-tel
            transaction.addToBackStack(null); // opcionálisan, hogy vissza lehessen lépni
            transaction.commit();
        });

        // Eladok gomb eseménykezelése
        saleButton.setOnClickListener(v -> {
            // --- AZ IF / ELSE ELLENŐRZÉS TÖRÖLVE ---
            // A kód most már azonnal továbblép

            // FragmentTransaction használata a navigációhoz
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, new SalesFragment());  // Replace a salesFragment-tel
            transaction.addToBackStack(null); // opcionálisan, hogy vissza lehessen lépni
            transaction.commit();
        });
    }
}
