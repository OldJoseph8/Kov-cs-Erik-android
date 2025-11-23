package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.BuyAdapter;
import com.example.piacpalota.u.i.buylist.CarRepository; // Ez kell az adatokhoz
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

        // --- ADATOK BETÖLTÉSE A KÖZÖS TÁROLÓBÓL ---
        // Így látni fogod azokat az autókat is, amiket a SalesFragment-en adtál hozzá
        buyList = CarRepository.getInstance().getProducts();
        // -------------------------------------------

        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            // 1. KOSÁRBA GOMB
            @Override
            public void onAddToCartClick(Product product) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", product.getName());
                    bundle.putString("productPrice", product.getPrice());
                    bundle.putString("productQuantity", product.getQuantity());
                    bundle.putString("productLocation", product.getLocation());

                    // KÉPEK ÁTADÁSA (Lista formátumban!)
                    // Mivel a Bundle ArrayList-et vár, átalakítjuk a List-et ArrayList-té
                    bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

                    Navigation.findNavController(view).navigate(R.id.shoppingFragment, bundle);
                } catch (Exception e) {
                    Log.e("BuyFragment", "Navigációs hiba (Kosár): " + e.getMessage());
                }
            }

            // 2. RÉSZLETEK GOMB
            @Override
            public void onDetailsClick(Product product) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", product.getName());
                    bundle.putString("productPrice", product.getPrice());
                    bundle.putString("productQuantity", product.getQuantity());
                    bundle.putString("productLocation", product.getLocation());

                    // KÉPEK ÁTADÁSA (Lista formátumban!)
                    bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

                    // Navigálás a részletes adatlapra
                    Navigation.findNavController(view).navigate(R.id.carDetailFragment, bundle);
                } catch (Exception e) {
                    Log.e("BuyFragment", "Navigációs hiba (Részletek): " + e.getMessage());
                }
            }
        });

        recyclerView.setAdapter(buyAdapter);

        return view;
    }

    // Fontos: Ha visszalépsz a listára, frissíteni kell a nézetet,
    // hogy az újonnan felvett autók megjelenjenek.
    @Override
    public void onResume() {
        super.onResume();
        if (buyAdapter != null) {
            buyAdapter.notifyDataSetChanged();
        }
    }
}