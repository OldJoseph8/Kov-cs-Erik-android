package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.piacpalota.MainActivity;
import com.example.piacpalota.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnEnter = view.findViewById(R.id.btnEnter);

        btnEnter.setOnClickListener(v -> {
            // Mivel átugorjuk a bejelentkezést, itt beállítjuk,
            // hogy "bent vagyunk" (a menü miatt fontos)
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.isUserLoggedIn = true;
                mainActivity.invalidateOptionsMenu();
            }

            // Közvetlenül a WelcomeFragment-re (a választó oldalra) lépünk!
            try {
                Navigation.findNavController(v).navigate(R.id.welcomeFragment);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
