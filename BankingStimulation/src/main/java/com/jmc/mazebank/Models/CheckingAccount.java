package com.jmc.mazebank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CheckingAccount extends Account {
    // The number of Transactions a client is allowed to do per day
    private final IntegerProperty transactionLimit;
    public CheckingAccount(String owner, String accountNumber, double balance, int limit) {
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleIntegerProperty(this, "TransactionLimit", limit);
    }

    public IntegerProperty transactionLimitProperty() { return this.transactionLimit; }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "transactionLimit=" + transactionLimit +
                '}';
    }

}
