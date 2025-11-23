package com.example.piacpalota.u.i;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.buylist.Product;

import java.util.ArrayList; // Fontos import!
import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartItems;
    private TextView emptyCartMessage;
    private Button submitOrderButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyCartMessage = view.findViewById(R.id.empty_cart_message);
        submitOrderButton = view.findViewById(R.id.button_submit_order);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItems = CartManager.getInstance().getCartItems();

        cartAdapter = new CartAdapter(getContext(), cartItems, new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onQuantityChange(Product product, String newQuantity) {
                product.setQuantity(newQuantity);
                cartAdapter.notifyItemChanged(cartItems.indexOf(product));
                updateCartView();
            }

            @Override
            public void onDelete(Product product) {
                int position = cartItems.indexOf(product);
                if (position != -1) {
                    CartManager.getInstance().removeFromCart(product);
                    // Nem kell a cartItems.remove(position), mert a CartManager referencia ugyanaz!
                    // De a biztonság kedvéért frissítjük az adaptert.
                    cartAdapter.notifyItemRemoved(position);
                    updateCartView();
                } else {
                    Toast.makeText(getContext(), "Elem nem található a kosárban!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(cartAdapter);

        // Adatok fogadása az érkező Bundle-ből
        Bundle bundle = getArguments();
        if (bundle != null) {
            String productName = bundle.getString("productName");
            String productPrice = bundle.getString("productPrice");
            String productQuantity = bundle.getString("productQuantity");
            String productLocation = bundle.getString("productLocation");

            // --- ITT A JAVÍTÁS: Képek listájának átvétele ---
            ArrayList<String> productImages = bundle.getStringArrayList("productImages");

            // Ha véletlenül null jönne (régi hívás), kezeljük le:
            if (productImages == null) {
                productImages = new ArrayList<>();
                productImages.add("https://via.placeholder.com/150"); // Alapértelmezett kép
            }

            // Termék hozzáadása a kosárhoz (Most már listát adunk át!)
            Product product = new Product(productName, productPrice, productQuantity, productLocation, productImages);

            // Ellenőrizzük, hogy ne adjuk hozzá duplán, ha már benne van
            boolean alreadyInCart = false;
            for (Product p : cartItems) {
                if (p.getName().equals(productName)) { // Egyszerű ellenőrzés név alapján
                    alreadyInCart = true;
                    break;
                }
            }

            if (!alreadyInCart) {
                CartManager.getInstance().addToCart(product);
                // A cartItems referencia automatikusan frissül, ha a CartManager ugyanazt a listát használja
                cartAdapter.notifyDataSetChanged();
            }
            // ------------------------------------------------
        }

        updateCartView();

        submitOrderButton.setOnClickListener(v -> submitOrder());

        return view;
    }

    private void updateCartView() {
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartMessage.setVisibility(View.VISIBLE);
            submitOrderButton.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartMessage.setVisibility(View.GONE);
            submitOrderButton.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void submitOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "A kosár üres, nem lehet rendelést leadni.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Product product : cartItems) {
            sendOrderToAdvertiser(product);
        }

        CartManager.getInstance().clearCart();
        cartAdapter.notifyDataSetChanged();
        updateCartView();

        Toast.makeText(getContext(), "A rendelés sikeresen leadva!", Toast.LENGTH_SHORT).show();
    }

    private void sendOrderToAdvertiser(Product product) {
        // Placeholder
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter.notifyDataSetChanged();
        updateCartView();
    }
}