/**
 * Service level feature implementations
 */

package com.example.demo.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dao.VendingDAO;
import com.example.demo.model.Coin;
import com.example.demo.model.Product;
import com.example.demo.model.Sales;
import com.example.demo.service.VendingMachineService;

@Component
public class VendingMachineServiceImpl implements VendingMachineService {

	private List<Product> prodInventory;
	private List<Coin> cashInventory;
	private StateTransitions stateTrans;
	private String machineState;
	private ProductCatalogService prodCatalog;
	private int userCoins;
	@Autowired
	private VendingDAO vendingDAO;

	public VendingMachineServiceImpl() {
		prodCatalog = new ProductCatalogService();
		initInventory();
		initMachineState();
		userCoins = 0;
	}

	/**
	 * add some products for the 
	 * system to display during start
	 */
	private void initProductInventory() {
		this.prodInventory = new ArrayList<Product>();
		// lets add 10 coke
		// 10 - pepsi
		// 10 - fanta
		for (int i = 0; i < 2; i++) {
			this.prodInventory.add(prodCatalog.getCatalog().get(1));
			this.prodInventory.add(prodCatalog.getCatalog().get(2));
			this.prodInventory.add(prodCatalog.getCatalog().get(3));
			//this.prodInventory.add(prodCatalog.getCatalog().get(4));
		}
	}

	/**
	 * 
	 * add some cash for the 
	 * system to display during start
	 */
	private void initCashInventory() {
		this.cashInventory = new ArrayList<Coin>();
		// add 10 quarters
		for (int i = 0; i < 10; i++) {
			Coin coin = new Coin(Double.valueOf(0.25));
			this.cashInventory.add(coin);
		}
	}

	private void initInventory() {
		// initialize item inventory
		this.initProductInventory();
		// initialize coin inventory
		this.initCashInventory();
		displayInventory();
	}

	/**
	 * 
	 * initialize the machine state a/c
	 * to the state transition diagram
	 */
	private void initMachineState() {
		this.stateTrans = new StateTransitions();
		this.machineState = this.stateTrans.getNextState("init", null);
	}

	@Override
	public List<Product> getProductInventory() {
		return this.prodInventory;
	}

	@Override
	public List<Coin> getCashInventory() {
		return this.cashInventory;
	}

	@Override
	public double calculateChange(int prodID, int quarters) {
		double totalChange = quarters * 0.25 - this.prodCatalog.getCatalog().get(prodID).getPrice();
		return totalChange;
	}

	@Override
	public void addProduct(Product prod) {
		this.prodInventory.add(prod);
	}

	@Override
	public void removeProduct(Product prod) throws IndexOutOfBoundsException {
		this.prodInventory.remove(prod);
	}

	@Override
	public void addCash(Coin coin) {
		this.cashInventory.add(coin);
	}

	@Override
	public void removeCash(Coin coin) throws IndexOutOfBoundsException {
		this.cashInventory.remove(coin);
	}

	/**
	 * update the state when some action
	 * occurs in the current state
	 */
	@Override
	public void updateState(String action) {
		String newState = this.stateTrans.getNextState(this.machineState, action);
		this.machineState = newState;
		if (newState == "soda_sold") {
			if (this.prodInventory.size() == 0) {
				this.updateState("no_soda");
			} else {
				this.updateState("yes_soda");
			}
		}
	}

	@Override
	public int insertUserCoins() {
		this.userCoins+=1;
		return this.userCoins;
	}

	@Override
	public int ejectUserCoins() {
		this.userCoins=0;
		return this.userCoins;
	}
	
	/**
	 * record the sales event, triggering
	 * 1) remove the product from inventory,
	 * 2) adding cash to the cash inventory,
	 * 3) persisting the sales event in DB,
	 * 4) reducing the userCoins count,
	 * 5) displaying the current inventory in console
	 * 
	 */
	@Override
	public void recordSales(int prodID) {
		// TODO: add to DB
		// change coins count and product inventory
		Product soldProd = this.prodCatalog.getCatalog().get(prodID);
		this.removeProduct(soldProd);
		this.addCash(new Coin(soldProd.getPrice()));
		this.persistSale(soldProd.getId(), soldProd.getName(), soldProd.getPrice());
		//reduce usercoins
		this.userCoins-=1;
		this.displayInventory();
	}

	public void persistSale(int prodId, String prodName, double prodPrice) {
		boolean recordInserted = vendingDAO.addSale(prodId, prodName, prodPrice);
	}

	/**
	 * 
	 * display the inventory in the console
	 * 
	 */
	public void displayInventory() {
		// print the latest product inventory
		System.out.println("***************");
		System.out.println("*****LATEST INVENTORY*********");
		System.out.println("***************");
		HashMap<Integer, Integer> prodCountsMap = new HashMap();
		for (Product prod : this.prodInventory) {
			prodCountsMap.put(prod.getId(), prodCountsMap.getOrDefault(prod.getId(), 0) + 1);
		}
		for (Entry<Integer, Integer> prod : prodCountsMap.entrySet()) {
			System.out.println("Product:" + prod.getKey() + " counts:" + prod.getValue());
		}
		// print the latest coinsCountMap balance
		HashMap<Double, Integer> coinsCountMap = new HashMap();
		for (Coin coin : this.cashInventory) {
			coinsCountMap.put(coin.getValue(), coinsCountMap.getOrDefault(coin.getValue(), 0) + 1);
		}
		for (Entry<Double, Integer> coin : coinsCountMap.entrySet()) {
			System.out.println("Coin:" + coin.getKey() + " counts:" + coin.getValue());
		}
		System.out.println("***************");
	}

	public LinkedHashMap<String, String> getProductList() {
		LinkedHashMap<String, String> prodList = new LinkedHashMap<>();
		for (Product prod : this.getProductInventory()) {
			prodList.put(String.valueOf(prod.getId()), prod.getName());
		}
		return prodList;
	}

	public LinkedHashMap<String, String> getCoinList() {
		LinkedHashMap<String, String> cashList = new LinkedHashMap<>();
		for (Coin coin : this.getCashInventory()) {
			cashList.put(String.valueOf(coin.getValue()), String.valueOf(coin.getValue()));
		}
		return cashList;
	}

	@Override
	public int getUserCoinsCount() {
		return this.userCoins;
	}

	@Override
	public String getCurrentState() {
		return this.machineState;
	}

	@Override
	public List<Sales> getSalesList() {
		return vendingDAO.getSaleReport();
	}

}
