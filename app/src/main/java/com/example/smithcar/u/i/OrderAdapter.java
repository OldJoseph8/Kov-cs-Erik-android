package com.example.smithcar.u.i;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smithcar.R;
import com.example.smithcar.u.i.buylist.Product;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Product> orderList;
    private OnOrderDeleteListener onOrderDeleteListener;

    public interface OnOrderDeleteListener {
        void onOrderDelete(Product product);
    }

    public OrderAdapter(Context context, List<Product> orderList, OnOrderDeleteListener onOrderDeleteListener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderDeleteListener = onOrderDeleteListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Product product = orderList.get(position);
        holder.orderName.setText(product.getName());
        holder.orderPrice.setText("Ár: " + product.getPrice());
        holder.orderLocation.setText("Helyszín: " + product.getLocation());
        holder.deleteButton.setOnClickListener(v -> onOrderDeleteListener.onOrderDelete(product));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderPrice, orderLocation;
        Button deleteButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderName = itemView.findViewById(R.id.order_name);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderLocation = itemView.findViewById(R.id.order_location);
            deleteButton = itemView.findViewById(R.id.delete_order_button);
        }
    }
}
