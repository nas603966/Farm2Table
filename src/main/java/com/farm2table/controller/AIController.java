package com.farm2table.controller;

import com.farm2table.ai.DynamicPricingService;
import com.farm2table.ai.FraudDetectionService;
import com.farm2table.ai.RecommendationService;
import com.farm2table.ai.PricingInsightsService;
import com.farm2table.dto.FraudRequest;
import com.farm2table.dto.PriceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private DynamicPricingService pricingService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private FraudDetectionService fraudService;

    @Autowired
    private PricingInsightsService insightsService;

    @Operation(summary = "Get optimal price", description = "Calculates and returns an optimal price for the product.")
    @PostMapping("/price")
    public ResponseEntity<?> getOptimalPrice(@RequestBody PriceRequest request) {
        if (request == null || !request.isValid()) {
            return ResponseEntity.badRequest().body("Invalid request. Historical prices and demand data cannot be empty.");
        }

        double optimalPrice = pricingService.calculateOptimalPrice(request.getHistoricalPrices(), request.getDemand());
        return ResponseEntity.ok(optimalPrice);
    }

    @Operation(summary = "Get product recommendations", description = "Returns personalized product recommendations based on user preferences and behavior.")
    @GetMapping("/recommend/{userId}")
    public ResponseEntity<?> getRecommendations(
            @Parameter(description = "User ID to fetch recommendations for") @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {

        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().body("Invalid user ID.");
        }

        List<Long> recommendations = recommendationService.getRecommendedProducts(userId, limit);
        if (recommendations.isEmpty()) {
            return ResponseEntity.ok("No recommendations found for user: " + userId);
        }
        return ResponseEntity.ok(recommendations);
    }

    @Operation(summary = "Get pricing insights", description = "Returns detailed pricing insights including historical trends and demand analysis.")
    @GetMapping("/price/insights")
    public ResponseEntity<?> getPricingInsights(
            @Parameter(description = "Product ID to analyze") @RequestParam Long productId,
            @Parameter(description = "Time period in days") @RequestParam(defaultValue = "30") int days) {
        
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("Invalid product ID.");
        }
        
        if (days <= 0 || days > 365) {
            return ResponseEntity.badRequest().body("Invalid time period. Must be between 1 and 365 days.");
        }
        
        // In a real implementation, you would fetch historical data from a database
        // For now, we'll use placeholder data
        List<Double> historicalPrices = List.of(10.0, 11.0, 10.5, 12.0, 11.5);
        List<Integer> demand = List.of(100, 95, 105, 90, 110);
        
        Map<String, Object> insights = insightsService.calculateInsights(historicalPrices, demand, days);
        insights.put("productId", productId);
        
        return ResponseEntity.ok(insights);
    }
    
    @Operation(summary = "Get pricing insights with custom data", description = "Returns detailed pricing insights based on provided historical data.")
    @PostMapping("/price/insights/custom")
    public ResponseEntity<?> getCustomPricingInsights(
            @RequestBody PriceRequest request,
            @Parameter(description = "Time period in days") @RequestParam(defaultValue = "30") int days) {
        
        if (request == null || !request.isValid()) {
            return ResponseEntity.badRequest().body("Invalid request. Historical prices and demand data cannot be empty.");
        }
        
        if (days <= 0 || days > 365) {
            return ResponseEntity.badRequest().body("Invalid time period. Must be between 1 and 365 days.");
        }
        
        Map<String, Object> insights = insightsService.calculateInsights(
            request.getHistoricalPrices(), 
            request.getDemand(), 
            days
        );
        
        return ResponseEntity.ok(insights);
    }

    @Operation(summary = "Check for fraud", description = "Analyzes a transaction and returns if it's potentially fraudulent.")
    @PostMapping("/fraud")
    public ResponseEntity<?> checkFraud(@RequestBody FraudRequest request) {
        if (request == null || !request.isValid()) {
            return ResponseEntity.badRequest().body("Invalid fraud request data.");
        }

        boolean isFraudulent = fraudService.isTransactionFraudulent(request.getTransactionAmounts(), request.getNewTransaction());
        return ResponseEntity.ok(isFraudulent);
    }
}