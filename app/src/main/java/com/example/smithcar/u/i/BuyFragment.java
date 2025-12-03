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

        buyList = CarRepository.getInstance().getProducts();

        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            @Override
            public void onAddToCartClick(Product product) {
                navigateWithProduct(product, R.id.shoppingFragment);
            }

            @Override
            public void onDetailsClick(Product product) {
                // Ez visz a részletekhez!
                navigateWithProduct(product, R.id.carDetailFragment);
            }
        });

        recyclerView.setAdapter(buyAdapter);
        return view;
    }

    private void navigateWithProduct(Product product, int destinationId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productQuantity", product.getQuantity());
            bundle.putString("productLocation", product.getLocation());
            bundle.putString("productDescription", product.getDescription());

            // Elérhetőség hozzáadása (fontos a kapcsolat gombhoz!)
            // Ha nincs a Product-ban, alapértelmezettet adunk
            // (Ideiglenesen, amíg a Product osztályt nem frissíted teljesen)
            bundle.putString("productContact", "+36 30 123 4567");

            bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateTo(destinationId, bundle);
            }
        } catch (Exception e) {
            Log.e("BuyFragment", "Hiba: " + e.getMessage());
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