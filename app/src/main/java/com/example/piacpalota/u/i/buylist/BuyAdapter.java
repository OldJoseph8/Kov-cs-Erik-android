package com.example.piacpalota.u.i.buylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;

import java.util.List;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.BuyViewHolder> {

    private final Context context;
    private final List<Product> buyList;
    private final OnProductClickListener onProductClickListener;

    // MÓDOSÍTVA: Két metódus van, egy a kosárnak, egy a részleteknek
    public interface OnProductClickListener {
        void onAddToCartClick(Product product);
        void onDetailsClick(Product product);
    }

    public BuyAdapter(Context context, List<Product> buyList, OnProductClickListener onProductClickListener) {
        this.context = context;
        this.buyList = buyList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public BuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buy, parent, false);
        return new BuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyViewHolder holder, int position) {
        Product product = buyList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productLocation.setText(product.getLocation());
        holder.productQuantity.setText(product.getQuantity());

        // Kép betöltése Glide használatával
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.productImage);

        // MÓDOSÍTVA: Kosár gomb eseménykezelője
        holder.cartButton.setOnClickListener(v -> {
            onProductClickListener.onAddToCartClick(product);
        });

        // MÓDOSÍTVA: Részletek gomb eseménykezelője
        holder.detailsButton.setOnClickListener(v -> {
            onProductClickListener.onDetailsClick(product);
        });
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    // JAVÍTVA: A belső osztály most már helyes
    public static class BuyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productLocation;
        TextView productQuantity;
        Button cartButton;
        Button detailsButton; // <--- ÚJ MEZŐ

        public BuyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productLocation = itemView.findViewById(R.id.product_location);
            productQuantity = itemView.findViewById(R.id.product_quantity);

            // Győződj meg róla, hogy az item_buy.xml-ben ezek az ID-k vannak!
            cartButton = itemView.findViewById(R.id.button_add_to_cart); // Vagy add_to_cart_button, ellenőrizd az XML-t!
            detailsButton = itemView.findViewById(R.id.btnDetails);      // <--- ÚJ GOMB BEKÖTÉSE
        }
    }
}