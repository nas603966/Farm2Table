package com.farm2table.ai;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FraudDetectionService {

    private static final double FRAUD_THRESHOLD = 1.5;
    private static final int MINIMUM_DATA_SIZE = 2; // Minimum data points required for fraud detection

    public boolean isTransactionFraudulent(List<Double> transactionAmounts, double newTransaction) {
        if (transactionAmounts.size() < MINIMUM_DATA_SIZE) {
            return false;  // Not enough data to make a fraud determination
        }

        double mean = transactionAmounts.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = transactionAmounts.stream().mapToDouble(amount -> Math.pow(amount - mean, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        double deviation = Math.abs(newTransaction - mean);
        return deviation > (FRAUD_THRESHOLD * stdDev);
    }
}