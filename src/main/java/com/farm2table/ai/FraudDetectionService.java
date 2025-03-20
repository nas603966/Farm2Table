package com.farm2table.ai;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FraudDetectionService {

    private static final double FRAUD_THRESHOLD = 1.5;

    public boolean isTransactionFraudulent(List<Double> transactionAmounts, double newTransaction) {
        double mean = transactionAmounts.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = transactionAmounts.stream().mapToDouble(amount -> Math.pow(amount - mean, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        double deviation = Math.abs(newTransaction - mean);
        return deviation > (FRAUD_THRESHOLD * stdDev);
    }
}
