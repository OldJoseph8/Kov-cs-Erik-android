package com.example.smithcar.u.i;

import android.annotation.SuppressLint;
import android.app.AlertDialog; // ÚJ IMPORT A DIALOGHOZ
import android.content.Intent; // ÚJ IMPORT
import android.net.Uri; // ÚJ IMPORT
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText; // ÚJ IMPORT
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

    // Tároljuk az elérhetőséget
    private String currentSellerContact = "+36 30 123 4567";

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
                    cartAdapter.notifyItemRemoved(position);
                    updateCartView();
                } else {
                    Toast.makeText(getContext(), "Elem nem található a kosárban!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(cartAdapter);

        // --- ADATOK FOGADÁSA ---
        Bundle bundle = getArguments();
        if (bundle != null) {
            String productName = bundle.getString("productName");
            String productPrice = bundle.getString("productPrice");
            String productQuantity = bundle.getString("productQuantity");
            String productLocation = bundle.getString("productLocation");

            String productDescription = bundle.getString("productDescription");
            if (productDescription == null) productDescription = "";

            // Elérhetőség kinyerése és mentése
            String productContact = bundle.getString("productContact");
            if (productContact != null && !productContact.isEmpty()) {
                currentSellerContact = productContact;
            }

            ArrayList<String> productImages = bundle.getStringArrayList("productImages");
            if (productImages == null) {
                productImages = new ArrayList<>();
                productImages.add("https://via.placeholder.com/150");
            }

            Product product = new Product(
                    productName,
                    productPrice,
                    productQuantity,
                    productLocation,
                    productDescription,
                    productContact, // Átadjuk a konstruktornak
                    productImages
            );

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

        // A gomb most már a DIALOG-ot hozza fel
        submitOrderButton.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                showContactDialog();
            } else {
                Toast.makeText(getContext(), "A kosár üres.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // --- A KAPCSOLAT ABLAK MEGJELENÍTÉSE ---
    private void showContactDialog() {
        try {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            // Itt használjuk a korábban létrehozott dialog_contact.xml-t
            View dialogView = inflater.inflate(R.layout.dialog_contact, null);

            TextView tvPhone = dialogView.findViewById(R.id.tvPhoneNumber);
            EditText etMessage = dialogView.findViewById(R.id.etMessage);

            if (tvPhone != null) {
                tvPhone.setText(currentSellerContact);
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("Rendelés és Kapcsolat")
                    .setView(dialogView)

                    // GOMB 1: HÍVÁS
                    .setPositiveButton("Hívás", (dialog, which) -> {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + currentSellerContact));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Hiba a hívásnál", Toast.LENGTH_SHORT).show();
                        }
                    })

                    // GOMB 2: RENDELÉS KÜLDÉSE (Üzenettel)
                    .setNeutralButton("Rendelés elküldése", (dialog, which) -> {
                        String message = "";
                        if (etMessage != null) {
                            message = etMessage.getText().toString();
                        }

                        // Kosár ürítése és visszajelzés
                        CartManager.getInstance().clearCart();
                        cartAdapter.notifyDataSetChanged();
                        updateCartView();

                        Toast.makeText(getContext(), "Rendelés és üzenet elküldve!\n" + message, Toast.LENGTH_LONG).show();
                    })

                    // GOMB 3: MÉGSE
                    .setNegativeButton("Mégse", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
    @Override
    public void onResume() {
        super.onResume();
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter.notifyDataSetChanged();
        updateCartView();
    }
}