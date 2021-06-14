/**
 * interface defining the methods used
 * by service class
 */
package com.example.demo.service;


import java.util.LinkedHashMap;
import java.util.List;

import com.example.demo.model.Coin;
import com.example.demo.model.Product;
import com.example.demo.model.Sales;

public interface VendingMachineService {

	/**
	 * 
	 * calculate the change to be provided
	 * during a sale transaction
	 * 
	 * @param prodID
	 * @param quarters
	 * @return
	 */
    double calculateChange(int prodID, int quarters);

    /**
     * 
     * get the list of product inventories
     * offered during the machine start/reset
     * @return
     */
    List<Product> getProductInventory();
    
    /**
     * 
     * get the list of products currently available
     * @return
     */
    LinkedHashMap<String, String> getProductList();
    
    /**
     * 
     * get the list of coins currently available
     * @return
     */
    LinkedHashMap<String, String> getCoinList();

    /**
     * 
     * get the cash inventory
     * @return
     */
	List<Coin> getCashInventory();

	/**
	 * 
	 * add a product to the system
	 * @param prod
	 */
    void addProduct(Product prod);

    /**
     * 
     * remove the given product from the available products
     * in the system
     * @param prod
     * @throws IndexOutOfBoundsException
     */
    void removeProduct(Product prod) throws IndexOutOfBoundsException;

    /**
     * 
     * add coin/cash to the system
     * @param coin
     */
    void addCash(Coin coin);

    /**
     * 
     * remove coin/cash from the system
     * @param coin
     * @throws IndexOutOfBoundsException
     */
    void removeCash(Coin coin) throws IndexOutOfBoundsException;
    
    /**
     * 
     * update the machine state
     * @param action
     */
    void updateState(String action);
    
    /**
     * 
     * insert coin by user
     * @return
     */
    int insertUserCoins();
    
    /**
     * 
     * eject coin by user
     * @return
     */
    int ejectUserCoins();
    
    /**
     * 
     * get the number of coins
     * the user has inserted
     * @return
     */
    int getUserCoinsCount();
    
    /**
     * 
     * get the current state of machine
     * @return
     */
    String getCurrentState();
    
    /**
     * 
     * record the sales for a given product
     * @param prodID
     */
    void recordSales(int prodID);
    
    /**
     * 
     * get the sales report
     * @return
     */
    List<Sales> getSalesList();
}
