package com.example.demo.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dao.VendingDAO;
import com.example.demo.model.Product;
import com.example.demo.model.Sales;
import com.example.demo.service.impl.ConfigurationParser;

@Component
public class VendingDAOImpl implements VendingDAO {
	
	@Autowired
	ConfigurationParser configParser;
	
	private Connection getDBConnection() {
		Connection con = null;
		String dbUrl = configParser.getDbUrl();
		String dbName = configParser.getDbName();
		String dbUser = configParser.getDbUser();
		String dbPwd = configParser.getDbPwd();
		String timeZone = configParser.getTimeZone();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vending", "test", "password");
			String url = dbUrl+"/"+dbName+"?"+timeZone;
			con = DriverManager.getConnection(url, dbUser, dbPwd);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
		
	}

	/**
	 * add the sale operation to DB
	 */
	@Override
	public boolean addSale(int prodId, String prodName, double price) {
		try {
			Connection con = getDBConnection();
			String sql = "INSERT into sale_event(product_id, product_name, product_price) values(?, ?, ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, prodId);// first parameter in the query
			stmt.setString(2, prodName);// second parameter
			stmt.setDouble(3, price);

			int i = stmt.executeUpdate();
			System.out.println(i + " records inserted");

			con.close();

		} catch (Exception e) {
			System.out.println("EXCEPTION persisting sales event:" + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * get the sales transaction details
	 */
	@Override
	public List<Sales> getSaleReport() {
		List<Sales> salesList = new ArrayList<Sales>();
		Connection con = getDBConnection();
		PreparedStatement ps=null;
		ResultSet rs=null;

		try {
			ps=con.prepareStatement("select * from sale_event order by sale_id desc");
			rs=ps.executeQuery();
	        while(rs.next())
	        {
	        	Sales sales = new Sales();
	        	sales.setSale_id(rs.getInt("sale_id"));
	        	sales.setSale_time(String.valueOf(rs.getTimestamp("sale_time")));
	        	Product prod = new Product();
	        	prod.setId(rs.getInt("product_id"));
	        	prod.setName(rs.getString("product_name"));
	        	prod.setPrice(rs.getDouble("product_price"));
	        	sales.setProd(prod);
	        	salesList.add(sales);
	            System.out.println(sales);

	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
		return salesList;
	}

}
