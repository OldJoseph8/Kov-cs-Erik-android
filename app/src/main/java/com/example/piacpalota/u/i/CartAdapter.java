package com.example.piacpalota.u.i;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Product> cartItems;
    private OnCartItemClickListener onCartItemClickListener;

    public interface OnCartItemClickListener {
        void onQuantityChange(Product product, String newQuantity);
        void onDelete(Product product);
    }

    public CartAdapter(Context context, List<Product> cartItems, OnCartItemClickListener onCartItemClickListener) {
        this.context = context;
        this.cartItems = cartItems;
        this.onCartItemClickListener = onCartItemClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productQuantity.setText(product.getQuantity());
        holder.productLocation.setText(product.getLocation());

        // --- ITT IS JAVÍTVA: getThumbnailUrl() ---
        Glide.with(context)
                .load(product.getThumbnailUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.productImage);
        // ----------------------------------------

        holder.quantityButton.setOnClickListener(v -> {
            String currentQuantityStr = product.getQuantity();
            int currentQuantity;
            try {
                currentQuantity = Integer.parseInt(currentQuantityStr.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                currentQuantity = 1;
            }
            int newQuantity = currentQuantity + 1;
            String newQuantityStr = currentQuantityStr.contains("kg") ? newQuantity + "kg" : newQuantity + "darab";
            onCartItemClickListener.onQuantityChange(product, newQuantityStr);
        });

        holder.deleteButton.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            onCartItemClickListener.onDelete(product);
            notifyItemRemoved(positionToRemove);
        });

        holder.messageButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("productName", product.getName());
            bundle.putString("productPrice", product.getPrice());
            bundle.putString("productLocation", product.getLocation());

            try {
                Navigation.findNavController(v).navigate(R.id.messagesFragment, bundle);
            } catch (Exception e) {
                Log.e("CartAdapter", "Navigációs hiba: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        TextView productLocation;
        Button quantityButton;
        Button deleteButton;
        ImageView messageButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productLocation = itemView.findViewById(R.id.product_location);
            quantityButton = itemView.findViewById(R.id.button_quantity);
            deleteButton = itemView.findViewById(R.id.button_delete);
            messageButton = itemView.findViewById(R.id.button_message);
        }
    }
}