/**
 * interface to define the DAO operations
 */

package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Sales;

public interface VendingDAO {
	
	/**
	 * 
	 * add the sale operation to DB using DB connection
	 * and standard query
	 * @param prodId
	 * @param prodName
	 * @param price
	 * @return
	 */
	public boolean addSale(int prodId, String prodName, double price);
	
	
	public List<Sales> getSaleReport();

}
