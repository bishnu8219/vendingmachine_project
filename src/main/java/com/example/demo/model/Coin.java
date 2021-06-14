/**
 * class representing a coin/bill object
 * accepted by the machine
 */
package com.example.demo.model;

public class Coin {
    private double value;

    public Coin(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

}
