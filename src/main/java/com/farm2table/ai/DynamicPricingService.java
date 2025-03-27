package com.farm2table.ai;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DynamicPricingService {

    public double calculateOptimalPrice(List<Double> historicalPrices, List<Integer> demand) {
        if (historicalPrices == null || historicalPrices.isEmpty() || demand == null || demand.isEmpty()) {
            throw new IllegalArgumentException("Insufficient data for price calculation.");
        }

        double avgPrice = historicalPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        int totalDemand = demand.stream().mapToInt(Integer::intValue).sum();
        double demandFactor = (double) totalDemand / demand.size();

        double priceAdjustment = 0.05 * demandFactor;

        double optimalPrice = avgPrice + priceAdjustment;
        double maxAllowedPrice = avgPrice * 1.1;
        double minAllowedPrice = avgPrice * 0.9;

        return Math.max(minAllowedPrice, Math.min(optimalPrice, maxAllowedPrice));
    }
}