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

import java.util.ArrayList;
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

        // A közös kosárkezelőt használjuk
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
                    cartAdapter.notifyItemRemoved(position);
                    updateCartView();
                } else {
                    Toast.makeText(getContext(), "Elem nem található a kosárban!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(cartAdapter);

        // --- ADATOK FOGADÁSA ÉS TERMÉK LÉTREHOZÁSA ---
        Bundle bundle = getArguments();
        if (bundle != null) {
            String productName = bundle.getString("productName");
            String productPrice = bundle.getString("productPrice");
            String productQuantity = bundle.getString("productQuantity");
            String productLocation = bundle.getString("productLocation");

            // 1. JAVÍTÁS: Kivesszük a leírást is
            String productDescription = bundle.getString("productDescription");
            if (productDescription == null) productDescription = ""; // Biztonsági ellenőrzés

            // 2. Képek listájának átvétele
            ArrayList<String> productImages = bundle.getStringArrayList("productImages");
            if (productImages == null) {
                productImages = new ArrayList<>();
                productImages.add("https://via.placeholder.com/150");
            }

            // 3. JAVÍTÁS: A konstruktorba beillesztjük a 'productDescription'-t
            Product product = new Product(productName, productPrice, productQuantity, productLocation, productDescription, productImages);

            // Ellenőrizzük, hogy ne adjuk hozzá duplán
            boolean alreadyInCart = false;
            for (Product p : cartItems) {
                if (p.getName().equals(productName)) {
                    alreadyInCart = true;
                    break;
                }
            }

            if (!alreadyInCart) {
                CartManager.getInstance().addToCart(product);
                cartAdapter.notifyDataSetChanged();
            }
        }
        // ----------------------------------------------

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
            Toast.makeText(getContext(), "A kosár üres.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Itt lehetne elküldeni a rendelést...
        CartManager.getInstance().clearCart();
        cartAdapter.notifyDataSetChanged();
        updateCartView();
        Toast.makeText(getContext(), "Rendelés leadva!", Toast.LENGTH_SHORT).show();
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