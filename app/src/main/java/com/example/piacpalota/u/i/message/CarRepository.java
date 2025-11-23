package com.example.piacpalota.u.i.buylist;

import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private static CarRepository instance;
    private List<Product> productList;

    private CarRepository() {
        productList = new ArrayList<>();

        // 1. BMW
        List<String> bmwImages = new ArrayList<>();
        bmwImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/BMW_X5_%28G05%29_IMG_3953.jpg/1200px-BMW_X5_%28G05%29_IMG_3953.jpg");
        bmwImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/BMW_G05_IMG_0025.jpg/1200px-BMW_G05_IMG_0025.jpg");
        productList.add(new Product("BMW X5", "15.000.000 Ft", "1 db", "Budapest",
                "Ez egy kiváló állapotban lévő, keveset futott BMW X5. Full extra, bőr belső, panoráma tető. Csak garázsban tartott, nem dohányzó tulajdonostól.",
                bmwImages));

        // 2. Audi
        List<String> audiImages = new ArrayList<>();
        audiImages.add("https://upload.wikimedia.org/wikipedia/commons/3/32/2019_Audi_A4_35_TDi_S_Line_S-Tronic_2.0_Front.jpg");
        productList.add(new Product("Audi A4", "8.500.000 Ft", "1 db", "Debrecen",
                "Eladó szeretett Audi A4-esem. Rendszeresen karbantartott, szervizkönyves. Téli-nyári gumi szett jár hozzá.",
                audiImages));

        // 3. Mercedes
        List<String> mercImages = new ArrayList<>();
        mercImages.add("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Mercedes-Benz_W205_Mopf_IMG_3690.jpg/1200px-Mercedes-Benz_W205_Mopf_IMG_3690.jpg");
        productList.add(new Product("Mercedes C-Class", "12.000.000 Ft", "1 db", "Szeged",
                "Gyönyörű Mercedes C-osztály eladó. Megkímélt állapot, friss műszaki vizsga. Azonnal elvihető.",
                mercImages));
    }

    public static synchronized CarRepository getInstance() {
        if (instance == null) {
            instance = new CarRepository();
        }
        return instance;
    }

    public List<Product> getProducts() {
        return productList;
    }

    public void addProduct(Product product) {
        productList.add(product);
    }
}