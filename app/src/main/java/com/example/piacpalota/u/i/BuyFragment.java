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

        // Adatok betöltése
        buyList = CarRepository.getInstance().getProducts();

        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            @Override
            public void onAddToCartClick(Product product) {
                // Itt most a ShoppingFragment-et nyitjuk meg kézzel
                openFragment(new ShoppingFragment(), product);
            }

            @Override
            public void onDetailsClick(Product product) {
                // Itt pedig a CarDetailFragment-et (Részletek)
                openFragment(new CarDetailFragment(), product);
            }
        });

        recyclerView.setAdapter(buyAdapter);
        return view;
    }

    // --- EZ AZ ÚJ, "BOMBABIZTOS" KÉZI NAVIGÁCIÓ ---
    private void openFragment(Fragment fragment, Product product) {
        try {
            // 1. Adatok becsomagolása
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productQuantity", product.getQuantity());
            bundle.putString("productLocation", product.getLocation());
            bundle.putString("productDescription", product.getDescription());
            bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

            fragment.setArguments(bundle);

            // 2. A képernyő lecserélése "erőszakkal" (kikerülve a NavController-t)
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment) // A fő keretbe tesszük
                    .addToBackStack(null) // Hogy a vissza gomb működjön
                    .commit();

        } catch (Exception e) {
            Log.e("BuyFragment", "Hiba a megnyitáskor: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (buyAdapter != null) {
            buyAdapter.notifyDataSetChanged();
        }
    }
}