package com.farm2table.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FraudRequest {
    private List<Double> transactionAmounts;
    private double newTransaction;

}
