/**
 * class representing the state of the machine
 */

package com.example.demo.model;

public class MachineState {

    String name;
    String action;
    String nextState;

    public MachineState(String name) {
        this.name = name;
    }

    public MachineState(String name, String action, String nextState) {
        this.name = name;
        this.action = action;
        this.nextState = nextState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public String toString(){
        return "Current State is:"+this.getName();
    }
}
