package com.farm2table.controller;

import com.farm2table.ai.DynamicPricingService;
import com.farm2table.ai.FraudDetectionService;
import com.farm2table.ai.RecommendationService;
import com.farm2table.dto.FraudRequest;
import com.farm2table.dto.PriceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private DynamicPricingService pricingService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private FraudDetectionService fraudService;

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