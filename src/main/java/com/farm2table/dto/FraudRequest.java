package com.farm2table.dto;

import java.util.List;

public class FraudRequest {
    private List<Double> transactionAmounts;
    private double newTransaction;

    public List<Double> getTransactionAmounts() {
        return transactionAmounts;
    }

    public void setTransactionAmounts(List<Double> transactionAmounts) {
        this.transactionAmounts = transactionAmounts;
    }

    public double getNewTransaction() {
        return newTransaction;
    }

    public void setNewTransaction(double newTransaction) {
        this.newTransaction = newTransaction;
    }
}
