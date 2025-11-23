package com.example.piacpalota.u.i.buylist;

import java.util.List;

public class Product {
    private String name;
    private String price;
    private String quantity;
    private String location;
    private String description; // <--- ÚJ MEZŐ: Leírás
    private List<String> images; // <--- ÚJ MEZŐ: Képek listája

    // A konstruktor frissítve, hogy fogadja a leírást és a képlisát is
    public Product(String name, String price, String quantity, String location, String description, List<String> images) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.location = location;
        this.description = description;
        this.images = images;
    }

    // Getters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }
    public String getLocation() { return location; }
    public String getDescription() { return description; } // <--- ÚJ GETTER

    // Setters
    public void setQuantity(String quantity) { this.quantity = quantity; }

    // Képek lekérése
    public List<String> getImages() { return images; }

    // Segédfüggvény: Csak az első képet adja vissza (listákhoz, borítóképnek)
    public String getThumbnailUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }
        return ""; // Ha nincs kép, üreset ad vissza
    }
}