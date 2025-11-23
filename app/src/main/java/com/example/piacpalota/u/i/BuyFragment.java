package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.BuyAdapter;
import com.example.piacpalota.u.i.buylist.Product;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment {

    private RecyclerView recyclerView;
    private BuyAdapter buyAdapter;
    private List<Product> buyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // A 'view' változót itt hozzuk létre, ezt fogjuk használni a navigációhoz
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buyList = new ArrayList<>();

        // --- "KAMU" ADATOK ---
        buyList.add(new Product("BMW X5", "15.000.000 Ft", "1 db", "Budapest", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/BMW_X5_%28G05%29_IMG_3953.jpg/1200px-BMW_X5_%28G05%29_IMG_3953.jpg"));
        buyList.add(new Product("Audi A4", "8.500.000 Ft", "1 db", "Debrecen", "https://upload.wikimedia.org/wikipedia/commons/3/32/2019_Audi_A4_35_TDi_S_Line_S-Tronic_2.0_Front.jpg"));
        buyList.add(new Product("Mercedes C-Class", "12.000.000 Ft", "1 db", "Szeged", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Mercedes-Benz_W205_Mopf_IMG_3690.jpg/1200px-Mercedes-Benz_W205_Mopf_IMG_3690.jpg"));
        // ---------------------

        // Az Adapter beállítása a két gomb eseménykezelőjével
        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            // 1. KOSÁRBA GOMB KATTINTÁS
            @Override
            public void onAddToCartClick(Product product) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", product.getName());
                    bundle.putString("productPrice", product.getPrice());
                    bundle.putString("productQuantity", product.getQuantity());
                    bundle.putString("productLocation", product.getLocation());
                    bundle.putString("productImageUrl", product.getImageUrl());

                    // Navigálás a ShoppingFragment-re (Kosár)
                    Navigation.findNavController(view).navigate(R.id.shoppingFragment, bundle);
                } catch (Exception e) {
                    Log.e("BuyFragment", "Navigációs hiba (Kosár): " + e.getMessage());
                }
            }

            // 2. RÉSZLETEK GOMB KATTINTÁS (Ez hiányzott!)
            @Override
            public void onDetailsClick(Product product) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", product.getName());
                    bundle.putString("productPrice", product.getPrice());
                    bundle.putString("productQuantity", product.getQuantity());
                    bundle.putString("productLocation", product.getLocation());
                    bundle.putString("productImageUrl", product.getImageUrl());

                    // Navigálás a CarDetailFragment-re (Részletes adatlap)
                    Navigation.findNavController(view).navigate(R.id.carDetailFragment, bundle);
                } catch (Exception e) {
                    Log.e("BuyFragment", "Navigációs hiba (Részletek): " + e.getMessage());
                }
            }
        });

        recyclerView.setAdapter(buyAdapter);

        return view;
    }
}