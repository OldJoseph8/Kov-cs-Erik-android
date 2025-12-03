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

import com.example.smithcar.R;

public class MessagesFragment extends Fragment {

    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productLocationTextView;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        productNameTextView = view.findViewById(R.id.product_name);
        productPriceTextView = view.findViewById(R.id.product_price);
        productLocationTextView = view.findViewById(R.id.product_location);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_button);

        // Az adatok fogadása a bundle-ből
        if (getArguments() != null) {
            String productName = getArguments().getString("productName");
            String productPrice = getArguments().getString("productPrice");
            String productLocation = getArguments().getString("productLocation");

            productNameTextView.setText(productName);
            productPriceTextView.setText(productPrice);
            productLocationTextView.setText(productLocation);
        }

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();
            if (!message.isEmpty()) {
                // Logika az üzenet elküldéséhez
                // Itt hozzáadhatsz kódot az üzenet elküldéséhez a szerverre vagy adatbázisba

                Toast.makeText(getContext(), "Üzenet elküldve!", Toast.LENGTH_SHORT).show();
                messageEditText.setText(""); // Üzenet mező ürítése
            } else {
                Toast.makeText(getContext(), "Kérlek, írj be egy üzenetet!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
