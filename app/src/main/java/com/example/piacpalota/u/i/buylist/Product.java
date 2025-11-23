package com.example.piacpalota.u.i.buylist;

import java.util.List;

public class Product {
    private String name;
    private String price;
    private String quantity;
    private String location;
    private List<String> images; // Ez most már lista!

    public Product(String name, String price, String quantity, String location, List<String> images) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.location = location;
        this.images = images;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }
    public String getLocation() { return location; }

    // Visszaadja az összes képet
    public List<String> getImages() { return images; }

    // Visszaadja az ELSŐ képet (borítóképnek a listához)
    public String getThumbnailUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }
        return ""; // Üres, ha nincs kép
    }

    public void setQuantity(String newQuantity) {
    }
}