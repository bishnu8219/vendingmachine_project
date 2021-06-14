/**
 * class representing the Request operation when
 * the Vending Sale operation is requested
 */

package com.example.demo.model;

public class VendingRequest {
	
	public Integer id;
	
	private String usercoin;
	private String producttype;
	private String vendingresult;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsercoin() {
		return usercoin;
	}
	public void setUsercoin(String usercoin) {
		this.usercoin = usercoin;
	}
	
	public String getProducttype() {
		return producttype;
	}
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	
	public String getVendingresult() {
		return vendingresult;
	}
	public void setVendingresult(String vendingresult) {
		this.vendingresult = vendingresult;
	}
	
	public String toString() {
		return "VendingRequest, coins:"+getUsercoin()+" producttype:"+getProducttype();
	}
	
	
}

