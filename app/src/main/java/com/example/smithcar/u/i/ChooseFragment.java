package com.example.smithcar.u.i;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smithcar.R;
import com.example.smithcar.u.i.buylist.Product;

import java.util.List;

public class ChooseFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Product> orderItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Az eddigi rendeléseket beállítjuk, amelyeket a kosárból kaptunk
        orderItems = OrderManager.getInstance().getSubmittedOrders();

        if (orderItems.isEmpty()) {
            Log.d("ChooseFragment", "No orders found.");
        } else {
            Log.d("ChooseFragment", "Orders found: " + orderItems.size());
        }

        // Adapter beállítása a rendelési tételekhez
        orderAdapter = new OrderAdapter(getContext(), orderItems, this::onDeleteOrder);
        recyclerView.setAdapter(orderAdapter);

        return view;
    }

    private void onDeleteOrder(Product product) {
        // A rendelés eltávolítása a `OrderManager`-ből
        OrderManager.getInstance().removeOrder(product);

        // A lista frissítése és az adapter értesítése
        orderItems.remove(product);
        orderAdapter.notifyDataSetChanged();
    }
}
