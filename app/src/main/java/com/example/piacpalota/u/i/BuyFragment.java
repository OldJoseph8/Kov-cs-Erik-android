package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation; // <-- FONTOS: Új import
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// import com.example.piacpalota.MainActivity; // Erre már nincs szükség
import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.BuyAdapter;
import com.example.piacpalota.u.i.buylist.Product;
// --- FIREBASE IMPORT-ok TÖRÖLVE ---
// import com.google.firebase.firestore.FirebaseFirestore;
// import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment {

    private RecyclerView recyclerView;
    private BuyAdapter buyAdapter;
    private List<Product> buyList;
    // private FirebaseFirestore db; // TÖRÖLVE

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // db = FirebaseFirestore.getInstance(); // TÖRÖLVE
        buyList = new ArrayList<>();

        // --- MÓDOSÍTVA: "KAMU" ADATOK HOZZÁADÁSA A FIREBASE HELYETT ---
        // Később ezeket az adatokat lecserélhetjük a 'Car' adatmodellünkre
        buyList.add(new Product("BMW X5", "15.000.000 Ft", "1 db", "Budapest", "https://example.com/bmw.jpg"));
        buyList.add(new Product("Audi A4", "8.500.000 Ft", "1 db", "Debrecen", "https://example.com/audi.jpg"));
        buyList.add(new Product("Mercedes C-Class", "12.000.000 Ft", "1 db", "Szeged", "https://example.com/mercedes.jpg"));
        // -----------------------------------------------------------------


        buyAdapter = new BuyAdapter(getContext(), buyList, product -> {
            // Kosárba gomb adatátadása és navigáció
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productQuantity", product.getQuantity());
            bundle.putString("productLocation", product.getLocation());
            bundle.putString("productImageUrl", product.getImageUrl());

            // --- EZ A RÉSZ LETT JAVÍTVA ---
            // Helyi tranzakció (A régi, hibás kód):
            // ShoppingFragment shoppingFragment = new ShoppingFragment();
            // shoppingFragment.setArguments(bundle);
            // ((MainActivity) requireActivity()).replaceFragment(shoppingFragment);

            // ÚJ, MODERN NAVIGÁCIÓ:
            try {
                // A 'view' (a Fragment fő nézete) segítségével keressük a NavController-t
                Navigation.findNavController(view).navigate(R.id.shoppingFragment, bundle);
            } catch (Exception e) {
                Log.e("BuyFragment", "Navigációs hiba: " + e.getMessage());
                Toast.makeText(getContext(), "Hiba a kosár megnyitásakor", Toast.LENGTH_SHORT).show();
            }
            // --- JAVÍTÁS VÉGE ---
        });
        recyclerView.setAdapter(buyAdapter);

        // fetchProductsFromFirestore(); // TÖRÖLVE

        return view;
    }

    // --- AZ EGÉSZ fetchProductsFromFirestore() FÜGGVÉNYT TÖRÖLTÜK ---
}
