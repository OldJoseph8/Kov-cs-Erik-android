package com.example.smithcar.u.i;

import com.example.smithcar.u.i.buylist.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static OrderManager instance;
    private List<Product> submittedOrders;

    private OrderManager() {
        submittedOrders = new ArrayList<>();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Product product) {
        submittedOrders.add(product);
    }

    public List<Product> getSubmittedOrders() {
        return submittedOrders;
    }

    public void removeOrder(Product product) {
        submittedOrders.remove(product);
    }
}
