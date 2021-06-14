package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Product;
import com.example.demo.model.Sales;
import com.example.demo.model.VendingRequest;
import com.example.demo.model.VendingRequestResponse;
import com.example.demo.service.VendingMachineService;

@Controller
@RequestMapping("/vending")
public class DashBoardController {

	@Autowired
	VendingMachineService vendingMachineService;
	
	/**
	 * 
	 * model and return the view required to display the dashboard page
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		System.out.println("inside display dashboard method");
		
		LinkedHashMap<String, String> productTypeList = getProductTypesList();

		model.addAttribute("vendingrequest", new VendingRequest());
		model.addAttribute("productTypeList", productTypeList);
		// update the state
		vendingMachineService.updateState("init");
		return "/dashboard";
	}

	/**
	 * 
	 * model and return the JSON response when the dispenseproduct operation is
	 * triggered
	 * 
	 * @param vendingrequest
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/dispenseproduct", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public VendingRequestResponse performDispenseOperation(
			@ModelAttribute("vendingrequest") @Valid VendingRequest vendingrequest, BindingResult result,
			ModelMap model) {
		System.out.println("inside performSaleOperation method vendingrequest is: " + vendingrequest);
		if (result.hasErrors()) {
			System.out.println("ERROR: there is some problem!!!");
		}
		double changeAmount = 0.0;
		if (vendingMachineService.getCurrentState() == "out_of_soda") {
			System.out.println("Out of soda!!!");
		} else if (vendingMachineService.getUserCoinsCount() > 0) {
			System.out.println("User Coins " + vendingMachineService.getUserCoinsCount() + " found, dispensing!");
			// add the userCoins to vendingrequest
			vendingrequest.setUsercoin(String.valueOf(vendingMachineService.getUserCoinsCount()));
			// get change amount
			changeAmount = vendingMachineService.calculateChange(Integer.parseInt(vendingrequest.getProducttype()),
					vendingMachineService.getUserCoinsCount());
			// update the inventory
			vendingMachineService.recordSales(Integer.parseInt(vendingrequest.getProducttype()));
			// update the state
			vendingMachineService.updateState("push_soda_button");
			// if there are still user coins, retain the has_quarters state
			if (vendingMachineService.getUserCoinsCount() > 0) {
				vendingMachineService.updateState("inserts_quarter");
			}

		} else {
			System.out.println("User Coins not found, not dispensing!");
		}

		String response = getDispenseProductJSON(changeAmount);
		VendingRequestResponse vendingResponse = new VendingRequestResponse();
		if (result.hasErrors()) {
			Map<String, String> errors = result.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			vendingResponse.setValidated(false);
			vendingResponse.setErrorMessage(errors);
		} else {
			vendingResponse.setValidated(true);
			vendingrequest.setVendingresult(response);
			vendingResponse.setVendingrequest(vendingrequest);
			System.out.println("Sending response result as: " + vendingrequest.getVendingresult() + "...");
		}
		return vendingResponse;

	}

	/**
	 * 
	 * model and return JSON response when insertcoin operation is triggered
	 * 
	 * @param vendingrequest
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/insertcoin", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public VendingRequestResponse performAddCoin(@ModelAttribute("vendingrequest") @Valid VendingRequest vendingrequest,
			BindingResult result, ModelMap model) {
		System.out.println("inside performAddCoin method vendingrequest is: " + vendingrequest);
		if (result.hasErrors()) {
			System.out.println("ERROR: there is some problem!!!");
		}

		// update the state
		vendingMachineService.updateState("inserts_quarter");
		// insert quarters
		vendingMachineService.insertUserCoins();

		String response = getCoinsCountJSON();
		VendingRequestResponse vendingResponse = new VendingRequestResponse();
		if (result.hasErrors()) {
			Map<String, String> errors = result.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			vendingResponse.setValidated(false);
			vendingResponse.setErrorMessage(errors);
		} else {
			vendingResponse.setValidated(true);
			vendingrequest.setVendingresult(response);
			vendingResponse.setVendingrequest(vendingrequest);
			System.out.println("Sending response result as:" + vendingrequest.getVendingresult() + "...");
		}
		return vendingResponse;

	}

	/**
	 * 
	 * model and return the JSON response when ejectcoin operation is triggered
	 * 
	 * @param vendingrequest
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/ejectcoin", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public VendingRequestResponse performReturnCoin(
			@ModelAttribute("vendingrequest") @Valid VendingRequest vendingrequest, BindingResult result,
			ModelMap model) {
		System.out.println("inside performReturnCoin method vendingrequest is: " + vendingrequest);
		if (result.hasErrors()) {
			System.out.println("ERROR: there is some problem!!!");
		}

		// update the state
		vendingMachineService.updateState("ejects_quarter");
		// eject quarters
		vendingMachineService.ejectUserCoins();

		String response = getCoinsCountJSON();
		VendingRequestResponse vendingResponse = new VendingRequestResponse();
		if (result.hasErrors()) {
			Map<String, String> errors = result.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			vendingResponse.setValidated(false);
			vendingResponse.setErrorMessage(errors);
		} else {
			vendingResponse.setValidated(true);
			vendingrequest.setVendingresult(response);
			vendingResponse.setVendingrequest(vendingrequest);
			System.out.println("Sending response result as:" + vendingrequest.getVendingresult() + "...");
		}
		return vendingResponse;

	}

	/**
	 * 
	 * model and return JSON response when sodacount operation is triggered
	 * 
	 * @param vendingrequest
	 * @param result
	 * @param model
	 * @return
	 */

	@PostMapping(value = "/sodacount", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public VendingRequestResponse performSodaCount(
			@ModelAttribute("vendingrequest") @Valid VendingRequest vendingrequest, BindingResult result,
			ModelMap model) {
		System.out.println("inside performSodaCount method vendingrequest is: " + vendingrequest);
		if (result.hasErrors()) {
			System.out.println("ERROR: there is some problem!!!");
		}

		String response = getProductCountJSON();
		VendingRequestResponse vendingResponse = new VendingRequestResponse();
		if (result.hasErrors()) {
			Map<String, String> errors = result.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			vendingResponse.setValidated(false);
			vendingResponse.setErrorMessage(errors);
		} else {
			vendingResponse.setValidated(true);
			vendingrequest.setVendingresult(response);
			vendingResponse.setVendingrequest(vendingrequest);
			System.out.println("Sending response result as:" + vendingrequest.getVendingresult() + "...");
		}
		return vendingResponse;

	}
	
	/**
	 * 
	 * model and report the sales report in JSON format
	 * 
	 * @param vendingrequest
	 * @param result
	 * @param model
	 * @return
	 */
	
	@PostMapping(value = "/salesreport", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public VendingRequestResponse performSalesReport(
			@ModelAttribute("vendingrequest") @Valid VendingRequest vendingrequest, BindingResult result,
			ModelMap model) {
		System.out.println("inside performSalesReport method vendingrequest is: " + vendingrequest);
		if (result.hasErrors()) {
			System.out.println("ERROR: there is some problem!!!");
		}

		String response = getSalesReportJSON();
		VendingRequestResponse vendingResponse = new VendingRequestResponse();
		if (result.hasErrors()) {
			Map<String, String> errors = result.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			vendingResponse.setValidated(false);
			vendingResponse.setErrorMessage(errors);
		} else {
			vendingResponse.setValidated(true);
			vendingrequest.setVendingresult(response);
			vendingResponse.setVendingrequest(vendingrequest);
			System.out.println("Sending response result as:" + vendingrequest.getVendingresult() + "...");
		}
		return vendingResponse;

	}
	

	public LinkedHashMap<String, String> getProductTypesList() {
		return vendingMachineService.getProductList();
	}

	public LinkedHashMap<String, String> getCoinNames() {
		return vendingMachineService.getCoinList();
	}

	/**
	 * 
	 * format the product list and changes in JSON to display in the view
	 * 
	 * @param changeAmount
	 * @return
	 */
	public String getDispenseProductJSON(double changeAmount) {
		// return "{\"result\":{\"COKE\": 1, \"DIET-COKE\": 2}}";
		JSONObject jsonprod = new JSONObject();
		JSONObject jsonprodcount = new JSONObject();
		List<Product> prodInventory = vendingMachineService.getProductInventory();
		for (int i = 0; i < prodInventory.size(); i++) {
			Product prod = vendingMachineService.getProductInventory().get(i);
			jsonprod.put(prod.getName(), prod.getId());
			if(jsonprodcount.has(prod.getName())) {
				jsonprodcount.put(prod.getName(), 1 + jsonprodcount.getDouble(prod.getName()));
			}else {
				jsonprodcount.put(prod.getName(), 1);
			}
		}
		JSONObject jsonchange = new JSONObject();
		jsonchange.put("quarters", changeAmount / 0.25);
		JSONObject jsondata = new JSONObject();
		jsondata.put("products", jsonprod);
		jsondata.put("productcount", jsonprodcount);
		jsondata.put("change", jsonchange);

		JSONObject jsonresponse = new JSONObject();
		jsonresponse.put("result", jsondata);
		return jsonresponse.toString();
	}

	/**
	 * 
	 * prepare the sodacount json msg
	 * 
	 * @return
	 */

	public String getProductCountJSON() {
		JSONObject jsonprod = new JSONObject();
		List<Product> prodInventory = vendingMachineService.getProductInventory();
		for (int i = 0; i < prodInventory.size(); i++) {
			Product prod = vendingMachineService.getProductInventory().get(i);
			System.out.println("Inventory has product:" + prod);
			if (jsonprod.has(prod.getName())) {
				jsonprod.put(prod.getName(), 1 + jsonprod.getDouble(prod.getName()));
			} else {
				jsonprod.put(prod.getName(), 1);
			}

		}
		JSONObject jsondata = new JSONObject();
		jsondata.put("productcount", jsonprod);

		JSONObject jsonresponse = new JSONObject();
		jsonresponse.put("result", jsondata);
		return jsonresponse.toString();
	}
	
	/**
	 * 
	 * prepare sales report in JSON format
	 * 
	 * @return
	 */
	public String getSalesReportJSON() {
		List<Sales> salesList = vendingMachineService.getSalesList();
		JSONObject jsonsaleslist = new JSONObject();
		for(Sales sale:salesList) {
			JSONObject jsonsale = new JSONObject();
			jsonsale.put("sale_id", sale.getSale_id());
			jsonsale.put("product_id", sale.getProd().getId());
			jsonsale.put("product_name", sale.getProd().getName());
			jsonsale.put("product_price", sale.getProd().getPrice());
			jsonsale.put("sale_time", sale.getSale_time());
			jsonsaleslist.put(String.valueOf(sale.getSale_id()), jsonsale);
		}
		JSONObject jsondata = new JSONObject();
		jsondata.put("salesreport", jsonsaleslist);
		
		JSONObject jsonresponse = new JSONObject();
		jsonresponse.put("result", jsondata);
		return jsonresponse.toString();
	}
	

	/**
	 * 
	 * prepare the coin inserted json msg
	 * 
	 * @return
	 */

	public String getCoinsCountJSON() {
		JSONObject jsoncoins = new JSONObject();
		jsoncoins.put("coinscount", vendingMachineService.getUserCoinsCount());
		JSONObject jsonresponse = new JSONObject();
		jsonresponse.put("result", jsoncoins);
		return jsonresponse.toString();
	}

	/**
	 * 
	 * prepare the current machine state json msg
	 * 
	 * @return
	 */
	public String getMachineStateJSON() {
		JSONObject jsonstate = new JSONObject();
		jsonstate.put("machinestate", vendingMachineService.getCurrentState());
		JSONObject jsonresponse = new JSONObject();
		jsonresponse.put("result", jsonstate);
		return jsonresponse.toString();
	}

}
