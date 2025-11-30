package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment; // Fontos import!

import com.example.piacpalota.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ha a "Hirdetések" / "Hirdetek" gombos verziót használod (Egy gombos):
        Button btnEnter = view.findViewById(R.id.btnEnter);

        // Ha a régi Bejelentkezés / Regisztráció gombos verziót használod:
        Button loginButton = view.findViewById(R.id.welcBuyButton);
        Button regButton = view.findViewById(R.id.welcSaleButton);


        // --- 1. ESET: HA A "TOVÁBB AZ APPBA" GOMBOD VAN ---
        if (btnEnter != null) {
            btnEnter.setOnClickListener(v -> {
                try {
                    // Ez a 'welcomeFragment' ID-ra visz (kisbetűvel a nav_graph-ban)
                    NavHostFragment.findNavController(this).navigate(R.id.welcomeFragment);
                } catch (Exception e) {
                    Log.e("HomeFragment", "Navigációs hiba", e);
                }
            });
        }

        // --- 2. ESET: HA A RÉGI LOGIN/REGISZTRÁCIÓ GOMBJAID VANNAK ---
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                try {
                    // JAVÍTVA: A nav_graph.xml-ben: android:id="@+id/logInFragment"
                    // Tehát itt is logInFragment (nagy I-vel) kell!
                    NavHostFragment.findNavController(this).navigate(R.id.logInFragment);
                } catch (Exception e) {
                    Log.e("HomeFragment", "Hiba a Login navigációnál", e);
                }
            });
        }

        if (regButton != null) {
            regButton.setOnClickListener(v -> {
                try {
                    // JAVÍTVA: A nav_graph.xml-ben: android:id="@+id/singInFragment"
                    // Tehát itt is singInFragment (nagy I-vel) kell!
                    NavHostFragment.findNavController(this).navigate(R.id.singInFragment);
                } catch (Exception e) {
                    Log.e("HomeFragment", "Hiba a SignIn navigációnál", e);
                }
            });
        }

        return view;
    }
}