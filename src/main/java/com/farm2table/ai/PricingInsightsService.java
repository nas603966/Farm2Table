package com.farm2table.ai;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.DoubleSummaryStatistics;

@Service
public class PricingInsightsService {

    /**
     * Calculates pricing insights for a product over a specified time period
     * 
     * @param historicalPrices List of historical prices
     * @param demand List of demand values
     * @param days Time period in days
     * @return Map containing pricing insights
     */
    public Map<String, Object> calculateInsights(List<Double> historicalPrices, List<Integer> demand, int days) {
        Map<String, Object> insights = new HashMap<>();
        
        if (historicalPrices == null || historicalPrices.isEmpty() || 
            demand == null || demand.isEmpty()) {
            return insights;
        }
        
        // Calculate average price
        DoubleSummaryStatistics priceStats = historicalPrices.stream()
            .mapToDouble(Double::doubleValue)
            .summaryStatistics();
        
        double avgPrice = priceStats.getAverage();
        
        // Calculate price volatility (standard deviation)
        double variance = historicalPrices.stream()
            .mapToDouble(price -> Math.pow(price - avgPrice, 2))
            .average()
            .orElse(0.0);
        
        double volatility = Math.sqrt(variance);
        
        // Calculate demand trend
        String demandTrend = calculateDemandTrend(demand);
        
        // Populate insights
        insights.put("averagePrice", avgPrice);
        insights.put("priceVolatility", volatility);
        insights.put("demandTrend", demandTrend);
        insights.put("minPrice", priceStats.getMin());
        insights.put("maxPrice", priceStats.getMax());
        insights.put("timePeriod", days);
        
        return insights;
    }
    
    /**
     * Determines the demand trend based on historical demand data
     */
    private String calculateDemandTrend(List<Integer> demand) {
        if (demand.size() < 2) {
            return "insufficient data";
        }
        
        // Simple linear regression to determine trend
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;
        
        int n = demand.size();
        
        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += demand.get(i);
            sumXY += i * demand.get(i);
            sumXX += i * i;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        
        if (Math.abs(slope) < 0.1) {
            return "stable";
        } else if (slope > 0) {
            return "increasing";
        } else {
            return "decreasing";
        }
    }
} 