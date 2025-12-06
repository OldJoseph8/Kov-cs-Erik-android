package com.example.smithcar.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smithcar.MainActivity;
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

        // Adatok betöltése a közös tárolóból
        buyList = CarRepository.getInstance().getProducts();

        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            // Ha a "Kosárba" gombra nyomsz -> Irány a Kosár oldal
            @Override
            public void onAddToCartClick(Product product) {
                navigateWithProduct(product, R.id.shoppingFragment);
            }

            // Ha a "Részletek" gombra nyomsz -> Irány a Részletek oldal
            @Override
            public void onDetailsClick(Product product) {
                navigateWithProduct(product, R.id.carDetailFragment);
            }
        });

        recyclerView.setAdapter(buyAdapter);
        return view;
    }

    // Ez a függvény végzi a nehéz munkát: becsomagolja az adatokat és lapoz
    private void navigateWithProduct(Product product, int destinationId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productQuantity", product.getQuantity());
            bundle.putString("productLocation", product.getLocation());

            // Biztonságos adatátadás: Részletes leírás
            String desc = product.getDescription();
            bundle.putString("productDescription", desc != null && !desc.isEmpty() ? desc : "Nincs leírás.");

            // Képek átadása
            ArrayList<String> images = new ArrayList<>();
            if (product.getImages() != null) {
                images.addAll(product.getImages());
            }
            // Ha nincs kép, teszünk bele egyet, hogy ne legyen üres
            if (images.isEmpty()) {
                images.add("https://via.placeholder.com/400");
            }
            bundle.putStringArrayList("productImages", images);

            // Elérhetőség átadása
            // JAVÍTVA: Most már a Product objektumból vesszük ki az adatot
            String contact = product.getContactInfo();
            bundle.putString("productContact", contact != null && !contact.isEmpty() ? contact : "+36 30 123 4567");

            // Navigáció indítása
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(destinationId, bundle);
            } else {
                Log.e("BuyFragment", "Hiba: Nem találom a MainActivity-t!");
            }

        } catch (Exception e) {
            Log.e("BuyFragment", "Navigációs hiba: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Frissítjük a listát, ha visszatérünk (pl. új hirdetés után)
        if (buyAdapter != null) {
            buyAdapter.notifyDataSetChanged();
        }
    }
}