/**
 * class representing the Response
 * when the VendingRequest is sent to the system
 */

package com.example.demo.model;

import java.util.Map;

import com.example.demo.model.VendingRequest;

public class VendingRequestResponse {

	private VendingRequest vendingrequest;
	private boolean validated;
	private Map<String, String> errorMessage;

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public Map<String, String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Map<String, String> errorMessage) {
		this.errorMessage = errorMessage;
	}

	public VendingRequest getVendingrequest() {
		return vendingrequest;
	}

	public void setVendingrequest(VendingRequest vendingRequest) {
		this.vendingrequest = vendingRequest;
	}

}
