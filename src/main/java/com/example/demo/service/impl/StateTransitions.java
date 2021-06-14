/**
 * class to represent the state transitions
 */
package com.example.demo.service.impl;

public class StateTransitions {

	public StateTransitions() {
	}

	/**
	 * 
	 * state transitions mapping as shown
	 * in the state transition diagram
	 * 
	 * @param curState
	 * @param action
	 * @return
	 */
	public String getNextState(String curState, String action) {
		String nextState = null;
		
		if (curState=="init") {
			nextState = "no_quarter";
		}else if(curState=="no_quarter") {
			if (action=="init") {
				nextState = "no_quarter";
			}else if(action=="inserts_quarter") {
				nextState = "has_quarters";
			}else {
				nextState = "no_quarter";
			}
		}else if(curState == "has_quarters") {
			if(action=="init") {
				nextState = "has_quarters";
			}else if(action == "inserts_quarter") {
				nextState = "has_quarters";
			}else if(action == "ejects_quarter") {
				nextState = "no_quarter";
			}else if (action=="push_soda_button") {
				nextState = "soda_sold";
			}else {
				nextState = "no_quarter";
			}
		}else if(curState == "soda_sold") {
			if(action=="no_soda") {
				nextState = "out_of_soda";
			}else {
				nextState = "no_quarter";
			}
		}
		
		System.out.println("Next state for " + curState + " with action:" + action + " is:" + nextState);
		return nextState;
	}
}
