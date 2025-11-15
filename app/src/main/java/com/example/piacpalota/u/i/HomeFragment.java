package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.piacpalota.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button loginButton = view.findViewById(R.id.welcBuyButton);
        Button regButton = view.findViewById(R.id.welcSaleButton);

        // --- EZ A RÉSZ LETT JAVÍTVA (kis 'l', kis 'f') ---
        loginButton.setOnClickListener(v -> {
            try {
                // A 'nav_graph.xml'-ben szereplő helyes ID használata
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            } catch (Exception e) {
                Log.e("HomeFragment", "Navigációs hiba (LogIn)", e);
                Toast.makeText(getContext(), "Hoppá, hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- EZ A RÉSZ IS JAVÍTVA LETT (kis 's', kis 'f') ---
        regButton.setOnClickListener(v -> {
            try {
                // A 'nav_graph.xml'-ben szereplő helyes ID használata
                Navigation.findNavController(v).navigate(R.id.singinFragment);
            } catch (Exception e) {
                Log.e("HomeFragment", "Navigációs hiba (SingIn)", e);
                Toast.makeText(getContext(), "Hoppá, hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}