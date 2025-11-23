package com.example.piacpalota.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

        buyList = CarRepository.getInstance().getProducts();

        buyAdapter = new BuyAdapter(getContext(), buyList, new BuyAdapter.OnProductClickListener() {

            @Override
            public void onAddToCartClick(Product product) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", product.getName());
                    bundle.putString("productPrice", product.getPrice());
                    bundle.putString("productQuantity", product.getQuantity());
                    bundle.putString("productLocation", product.getLocation());
                    bundle.putString("productDescription", product.getDescription()); // <--- ÚJ
                    bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

                    Navigation.findNavController(view).navigate(R.id.shoppingFragment, bundle);
                } catch (Exception e) {
                    Log.e("BuyFragment", "Hiba: " + e.getMessage());
                }
            }

            @Override
            public void onDetailsClick(Product product) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", product.getName());
                    bundle.putString("productPrice", product.getPrice());
                    bundle.putString("productQuantity", product.getQuantity());
                    bundle.putString("productLocation", product.getLocation());
                    bundle.putString("productDescription", product.getDescription()); // <--- ÚJ
                    bundle.putStringArrayList("productImages", new ArrayList<>(product.getImages()));

                    Navigation.findNavController(view).navigate(R.id.carDetailFragment, bundle);
                } catch (Exception e) {
                    Log.e("BuyFragment", "Hiba: " + e.getMessage());
                }
            }
        });

        recyclerView.setAdapter(buyAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (buyAdapter != null) {
            buyAdapter.notifyDataSetChanged();
        }
    }
}