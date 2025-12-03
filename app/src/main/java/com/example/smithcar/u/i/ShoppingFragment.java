package com.example.smithcar.u.i;

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

import com.example.smithcar.R;
import com.example.smithcar.u.i.buylist.Product;

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
        // Betöltjük a kosár kinézetét (fragment_shopping.xml)
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyCartMessage = view.findViewById(R.id.empty_cart_message);
        submitOrderButton = view.findViewById(R.id.button_submit_order);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Kosár tartalmának lekérése a közös CartManager-ből
        cartItems = CartManager.getInstance().getCartItems();

        // Adapter beállítása (Törlés és mennyiség változtatás kezelése)
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

        // --- ADATOK FOGADÁSA (Ha a "Kosárba" gombbal jöttünk ide) ---
        Bundle bundle = getArguments();
        if (bundle != null) {
            String productName = bundle.getString("productName");
            String productPrice = bundle.getString("productPrice");
            String productQuantity = bundle.getString("productQuantity");
            String productLocation = bundle.getString("productLocation");

            String productDescription = bundle.getString("productDescription");
            if (productDescription == null) productDescription = "";

            String productContact = bundle.getString("productContact");
            if (productContact == null) productContact = "Nincs megadva";

            ArrayList<String> productImages = bundle.getStringArrayList("productImages");
            if (productImages == null) {
                productImages = new ArrayList<>();
                productImages.add("https://via.placeholder.com/150");
            }

            // Termék létrehozása az adatokból
            Product product = new Product(
                    productName,
                    productPrice,
                    productQuantity,
                    productLocation,
                    productDescription,
                    productContact,
                    productImages
            );

            // Ellenőrizzük, hogy ne adjuk hozzá duplán, ha már benne van
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

    // Ha üres a kosár, elrejtjük a listát és a gombot
    private void updateCartView() {
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            if (emptyCartMessage != null) emptyCartMessage.setVisibility(View.VISIBLE);
            if (submitOrderButton != null) submitOrderButton.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (emptyCartMessage != null) emptyCartMessage.setVisibility(View.GONE);
            if (submitOrderButton != null) submitOrderButton.setVisibility(View.VISIBLE);
        }
    }

    // Rendelés leadása (csak kiüríti a kosarat és üzenetet küld)
    @SuppressLint("NotifyDataSetChanged")
    private void submitOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "A kosár üres.", Toast.LENGTH_SHORT).show();
            return;
        }

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