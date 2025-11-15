package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piacpalota.MainActivity; // Ezt a LogInFragment-ed alapján adtam hozzá
import com.example.piacpalota.R;
// TÖRÖLTÜK A FIREBASE IMPORT-okat

public class SingInFragment extends Fragment {

    // private FirebaseAuth mAuth; // TÖRÖLVE
    // private FirebaseFirestore db; // TÖRÖLVE

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);

        // Firebase inicializálás TÖRÖLVE
        // mAuth = FirebaseAuth.getInstance();
        // db = FirebaseFirestore.getInstance();

        Button registerButton = view.findViewById(R.id.registButton);
        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        EditText nameEditText = view.findViewById(R.id.nameEditText); // Név mező

        // Regisztráció gomb kattintása
        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Csak azt ellenőrizzük, hogy nem üres-e
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- AZ EGÉSZ FIREBASE BLOKK TÖRÖLVE ---

            // Csak egy "siker" üzenetet mutatunk
            Toast.makeText(getActivity(), "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();

            // És azonnal továbblépünk a főoldalra (WelcomeFragment)
            // (Ezt a logikát a LogInFragment-edből vettem, amit korábban küldtél)
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.replaceFragment(new WelcomeFragment());
                mainActivity.invalidateOptionsMenu();  // Menü frissítése
            }
        });

        return view;
    }
}