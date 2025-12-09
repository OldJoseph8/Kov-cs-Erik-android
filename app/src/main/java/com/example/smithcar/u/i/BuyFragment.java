package com.example.smithcar.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction; // Fontos import a kézi lapozáshoz
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smithcar.R;
import com.example.smithcar.u.i.buylist.BuyAdapter;
import com.example.smithcar.u.i.buylist.CarRepository;
import com.example.smithcar.u.i.buylist.Product;

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

            // 1. KOSÁRBA GOMB (Kézi lapozással)
            @Override
            public void onAddToCartClick(Product product) {
                openFragment(new ShoppingFragment(), product);
            }

            // 2. RÉSZLETEK GOMB (Kézi lapozással)
            @Override
            public void onDetailsClick(Product product) {
                openFragment(new CarDetailFragment(), product);
            }
        });

        recyclerView.setAdapter(buyAdapter);
        return view;
    }

    // --- "BOMBABIZTOS" KÉZI LAPOZÓ FÜGGVÉNY ---
    // Ez kikerüli a NavController-t, és közvetlenül cseréli a képernyőt
    private void openFragment(Fragment fragment, Product product) {
        try {
            // Adatok becsomagolása
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productQuantity", product.getQuantity());
            bundle.putString("productLocation", product.getLocation());

            // Biztonsági ellenőrzések az adatoknál
            String desc = product.getDescription();
            bundle.putString("productDescription", desc != null ? desc : "");

            String contact = product.getContactInfo();
            bundle.putString("productContact", contact != null ? contact : "+36 12 345 6789");

            ArrayList<String> images = new ArrayList<>();
            if (product.getImages() != null) images.addAll(product.getImages());
            if (images.isEmpty()) images.add("https://via.placeholder.com/400");
            bundle.putStringArrayList("productImages", images);

            fragment.setArguments(bundle);

            // A képernyőcsere végrehajtása
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            // Az R.id.nav_host_fragment a fő keret a MainActivity-ben
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.addToBackStack(null); // Hogy a vissza gomb működjön
            transaction.commit();

        } catch (Exception e) {
            Log.e("BuyFragment", "Hiba a megnyitáskor: " + e.getMessage());
            Toast.makeText(getContext(), "Hiba a megnyitáskor", Toast.LENGTH_SHORT).show();
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