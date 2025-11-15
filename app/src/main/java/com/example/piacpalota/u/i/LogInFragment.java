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

public class LogInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        Button loginButton = view.findViewById(R.id.logButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.logPasswordTextText);

        // Bejelentkezés gomb kezelése
        loginButton.setOnClickListener(v -> { // <-- v (a View) fontos a navigációhoz
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Email és jelszó nem lehet üres", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getActivity(), "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();

            // --- EZ A RÉSZ MÓDOSULT ---

            // 1. Beállítjuk a "kamu" bejelentkezést a MainActivity-ben
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.isUserLoggedIn = true;
                mainActivity.invalidateOptionsMenu();  // Menü frissítése
            }

            // 2. Az ÚJ, modern navigációval lépünk tovább
            // A régi 'replaceFragment' hívás helyett
            try {
                // Megkeressük a NavController-t és elnavigálunk a WelcomeFragment-re
                // (Győződj meg róla, hogy a 'nav_graph.xml'-ben 'welcomeFragment' az ID-ja)
                Navigation.findNavController(v).navigate(R.id.welcomeFragment);
            } catch (Exception e) {
                // Hiba esetén kiírjuk, ha nem találja a célt
                Toast.makeText(getActivity(), "Navigációs hiba: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}