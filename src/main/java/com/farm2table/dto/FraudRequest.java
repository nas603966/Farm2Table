package com.farm2table.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class FraudRequest {
    private List<Double> transactionAmounts;
    private double newTransaction;

    public boolean isValid() {
        return transactionAmounts != null && !transactionAmounts.isEmpty() && newTransaction > 0;
    }
}
