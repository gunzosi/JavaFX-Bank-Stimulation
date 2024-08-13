package com.jmc.mazebank.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingAccount extends Account{
    // The draw limit from the saving
    private final DoubleProperty withdrawLimit;

    public SavingAccount(String owner, String accountNumber, double balance, double limit) {
        super(owner, accountNumber, balance);
        this.withdrawLimit = new SimpleDoubleProperty(this, "WithdrawLimit", limit);
    }

    public DoubleProperty withdrawLimitProperty() { return this.withdrawLimit; }
}
