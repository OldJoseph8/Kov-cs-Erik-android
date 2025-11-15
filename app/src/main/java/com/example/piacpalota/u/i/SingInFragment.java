package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation; // <-- FONTOS: Ezt az új importot add hozzá

import com.example.piacpalota.MainActivity;
import com.example.piacpalota.R;

public class SingInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);

        Button registerButton = view.findViewById(R.id.registButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        EditText nameEditText = view.findViewById(R.id.nameEditText);

        // Regisztráció gomb kattintása
        registerButton.setOnClickListener(v -> { // <-- A 'v' (View) kell a navigációhoz
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getActivity(), "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();

            // --- EZ A RÉSZ MÓDOSULT ---

            // 1. Beállítjuk a "kamu" bejelentkezést a MainActivity-ben
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.isUserLoggedIn = true;
                mainActivity.invalidateOptionsMenu();  // Menü frissítése
            }

            // 2. Az ÚJ, modern navigációval lépünk tovább
            try {
                // A 'v' (a gomb nézete) segítségével keressük meg a NavController-t
                Navigation.findNavController(v).navigate(R.id.welcomeFragment);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Navigációs hiba: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}