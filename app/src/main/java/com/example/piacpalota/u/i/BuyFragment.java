package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// FONTOS: Be kell importálni a MainActivity-t, hogy elérjük a navigateTo függvényt
import com.example.piacpalota.MainActivity;
import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.BuyAdapter;
import com.example.piacpalota.u.i.buylist.CarRepository;
import com.example.piacpalota.u.i.buylist.Product;

import java.util.ArrayList;
import java.util.List;

public class BuyFragment extends Fragment {

    private RecyclerView recyclerView;
    private BuyAdapter buyAdapter;
    private List<Product> buyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adatok betöltése a közös tárolóból
        // Így látni fogod a SalesFragment-en hozzáadott új autókat is
        buyList = CarRepository.getInstance().getProducts();

        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            // 1. KOSÁRBA GOMB
            @Override
            public void onAddToCartClick(Product product) {
                navigateWithProduct(product, R.id.shoppingFragment);
            }

            // 2. RÉSZLETEK GOMB - EZ VISZ ÁT AZ ÚJ OLDALRA!
            @Override
            public void onDetailsClick(Product product) {
                navigateWithProduct(product, R.id.carDetailFragment);
            }
        });

        recyclerView.setAdapter(buyAdapter);

        return view;
    }

    // Segédfüggvény a navigációhoz és adatátadáshoz
    // Ez csomagolja össze az autó adatait és kéri meg a MainActivity-t a lapozásra
    private void navigateWithProduct(Product product, int destinationId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productQuantity", product.getQuantity());
            bundle.putString("productLocation", product.getLocation());
            bundle.putString("productDescription", product.getDescription());

            // Képek listájának átadása
            // Fontos: ArrayList-té kell alakítani a List-et, mert a Bundle azt várja
            bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

            // A MainActivity-t kérjük meg a biztonságos lapozásra
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(destinationId, bundle);
            } else {
                Log.e("BuyFragment", "Hiba: Az Activity nem MainActivity!");
            }

        } catch (Exception e) {
            Log.e("BuyFragment", "Navigációs hiba: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Frissítjük a nézetet visszalépéskor, hogy az új adatok látszódjanak
        if (buyAdapter != null) {
            buyAdapter.notifyDataSetChanged();
        }
    }
}