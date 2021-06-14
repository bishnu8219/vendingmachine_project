/**
 * class representing the Product offered
 * by the machine
 */
package com.example.demo.model;

public class Product {

    private int id;
    private String name;
    private double price;

    public Product() {
    	
    }
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product(int itemID, String name, double price) {
        this.id = itemID;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public String toString() {
    	return "Product, id:"+String.valueOf(getId())+", name:"+getName()+", price:"+String.valueOf(getPrice());
    }

}
