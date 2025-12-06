package com.example.smithcar.u.i;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

// FONTOS: Ellenőrizd, hogy ez a saját projekted R osztálya!
import com.example.smithcar.R;

public class MessagesFragment extends Fragment {

    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productLocationTextView;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Betöltjük a layoutot (fragment_messages.xml)
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // UI elemek megkeresése
        productNameTextView = view.findViewById(R.id.product_name);
        productPriceTextView = view.findViewById(R.id.product_price);
        productLocationTextView = view.findViewById(R.id.product_location);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_button);

        // Adatok fogadása a Bundle-ből (amit a CartAdapter küldött)
        if (getArguments() != null) {
            String productName = getArguments().getString("productName");
            String productPrice = getArguments().getString("productPrice");
            String productLocation = getArguments().getString("productLocation");

            // Biztonsági ellenőrzés: Ha null, akkor írjunk ki valamit
            if (productName != null) productNameTextView.setText(productName);
            if (productPrice != null) productPriceTextView.setText(productPrice);
            if (productLocation != null) productLocationTextView.setText(productLocation);
        }

        // Küldés gomb eseménykezelője
        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();

            if (!message.isEmpty()) {
                // Itt lenne a logika az üzenet elküldéséhez (pl. szerverre)
                // Most csak egy Toast üzenetet dobunk fel
                Toast.makeText(getContext(), "Üzenet elküldve az eladónak!", Toast.LENGTH_SHORT).show();

                // Opcionális: Visszalépés a kosárhoz küldés után
                // requireActivity().onBackPressed();

                messageEditText.setText(""); // Mező ürítése
            } else {
                Toast.makeText(getContext(), "Kérlek, írj be egy üzenetet!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}