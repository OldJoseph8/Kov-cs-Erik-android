package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piacpalota.MainActivity;
import com.example.piacpalota.R;
// TÖRÖLTÜK A FIREBASE IMPORT-okat

public class LogInFragment extends Fragment {

    // private FirebaseAuth mAuth; // TÖRÖLVE

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infláljuk az XML layoutot
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Firebase Auth inicializálása TÖRÖLVE
        // mAuth = FirebaseAuth.getInstance();

        Button loginButton = view.findViewById(R.id.logButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText); // Email cím
        EditText passwordEditText = view.findViewById(R.id.logPasswordTextText); // Jelszó

        // Bejelentkezés gomb kezelése
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Csak azt ellenőrizzük, hogy nem üres-e
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Email és jelszó nem lehet üres", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- AZ EGÉSZ FIREBASE BLOKK TÖRÖLVE ---

            // Csak egy "siker" üzenetet mutatunk
            Toast.makeText(getActivity(), "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();

            // Menü frissítése a MainActivity-ben
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(new WelcomeFragment());
                mainActivity.invalidateOptionsMenu();  // Menü frissítése
            }
        });

        return view;
    }
}