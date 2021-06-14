/**
 * class to represent a Sales Transaction
 */
package com.example.demo.model;

public class Sales {

	int sale_id;
	Product prod;
	String sale_time;

	public Sales() {

	}

	public int getSale_id() {
		return sale_id;
	}

	public void setSale_id(int sale_id) {
		this.sale_id = sale_id;
	}

	public Product getProd() {
		return prod;
	}

	public void setProd(Product prod) {
		this.prod = prod;
	}

	public String getSale_time() {
		return sale_time;
	}

	public void setSale_time(String sale_time) {
		this.sale_time = sale_time;
	}
	
	public String toString() {
		return "Sales, id:"+String.valueOf(getSale_id())+", product_id:"+String.valueOf(getProd().getId())+", product_name:"+getProd().getName()+", product_price:"+String.valueOf(getProd().getPrice())+", sale_time:"+getSale_time();
	}

}
