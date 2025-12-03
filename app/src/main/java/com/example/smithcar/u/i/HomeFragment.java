package com.example.smithcar.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.smithcar.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // XML betöltése
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Csak az "Egy gombot" keressük
        Button btnEnter = view.findViewById(R.id.btnEnter);

        if (btnEnter != null) {
            btnEnter.setOnClickListener(v -> {
                try {
                    // Navigálás a 'welcomeFragment'-re (Választó oldal)
                    NavHostFragment.findNavController(this).navigate(R.id.welcomeFragment);
                } catch (Exception e) {
                    Log.e("HomeFragment", "Navigációs hiba", e);
                }
            });
        }

        return view;
    }
}