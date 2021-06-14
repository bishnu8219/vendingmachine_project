/**
 * class to represent the exhaustive Catalog of items
 * that can be offered by the system
 */
package com.example.demo.service.impl;

import java.util.HashMap;

import com.example.demo.model.Product;

public class ProductCatalogService {
    HashMap<Integer, Product> catalog;
    
    public ProductCatalogService(){
        catalog = new HashMap<Integer, Product>();
        Product prod1 = new Product(1, "COKE", 0.25);
        catalog.put(Integer.valueOf(1), prod1);
        Product prod2 = new Product(2, "PEPSI", 0.25);
        catalog.put(Integer.valueOf(2), prod2);
        Product prod3 = new Product(3, "DIET-COKE", 0.25);
        catalog.put(Integer.valueOf(3), prod3);
        Product prod4 = new Product(4, "FANTA", 0.25);
        catalog.put(Integer.valueOf(4), prod4);
    }

    public HashMap<Integer, Product> getCatalog() {
        return catalog;
    }

    public void setCatalog(HashMap<Integer, Product> catalog) {
        this.catalog = catalog;
    }
    
    
}
