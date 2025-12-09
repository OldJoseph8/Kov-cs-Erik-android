package com.example.smithcar.u.i;

import android.app.AlertDialog; // Fontos import a felugró ablakhoz
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smithcar.R;
import com.example.smithcar.u.i.buylist.Product;

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

        // Adatok beállítása
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productQuantity.setText(product.getQuantity());
        holder.productLocation.setText(product.getLocation());

        // Kép betöltése
        Glide.with(context)
                .load(product.getThumbnailUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.productImage);

        // Mennyiség gomb
        holder.quantityButton.setOnClickListener(v -> {
            // Egyszerű logika a szám növelésére (csak a szövegben)
            String current = product.getQuantity().replaceAll("[^0-9]", "");
            int qty = current.isEmpty() ? 1 : Integer.parseInt(current);
            onCartItemClickListener.onQuantityChange(product, (qty + 1) + " db");
        });

        // Törlés gomb
        holder.deleteButton.setOnClickListener(v -> {
            onCartItemClickListener.onDelete(product);
        });

        // --- EGYSZERŰSÍTETT MEGOLDÁS: FELUGRÓ ABLAK ---
        holder.messageButton.setOnClickListener(v -> {
            // Lekérjük az elérhetőséget (vagy alapértelmezettet adunk)
            String contact = product.getContactInfo();
            if (contact == null || contact.isEmpty()) {
                contact = "+36 30 123 4567\ninfo@smithcar.hu";
            }

            // Felugró ablak megjelenítése
            new AlertDialog.Builder(context)
                    .setTitle("Elérhetőség") // Az ablak címe
                    .setMessage("Az eladó elérhetőségei:\n\n" + contact) // A tartalom
                    .setPositiveButton("Rendben", null) // Bezáró gomb
                    .setIcon(android.R.drawable.ic_dialog_info) // Kis ikon
                    .show();
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
        ImageButton messageButton; // ImageButton, ahogy az XML-ben van!

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productLocation = itemView.findViewById(R.id.product_location);

            quantityButton = itemView.findViewById(R.id.detailQuantity);
            messageButton = itemView.findViewById(R.id.button_message);
        }
    }
}