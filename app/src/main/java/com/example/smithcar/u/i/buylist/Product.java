package com.example.smithcar.u.i.buylist;

import java.util.List;

public class Product {
    private String name;
    private String price;
    private String quantity;
    private String location;
    private String description;
    private String contactInfo; // <--- ÚJ MEZŐ: Elérhetőség (pl. telefonszám, email)
    private List<String> images;

    // Konstruktor bővítése
    public Product(String name, String price, String quantity, String location, String description, String contact, List<String> imagesToSave) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.location = location;
        this.description = description;
        this.contactInfo = contactInfo;
        this.images = images;
    }

    // Getters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getContactInfo() { return contactInfo; } // <--- ÚJ GETTER

    // Setters
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public List<String> getImages() { return images; }

    public String getThumbnailUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }
        return "";
    }
}