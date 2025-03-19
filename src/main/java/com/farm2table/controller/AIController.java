package com.farm2table.controller;

import com.farm2table.ai.DynamicPricingService;
import com.farm2table.ai.FraudDetectionService;
import com.farm2table.ai.RecommendationService;
import com.farm2table.dto.FraudRequest;
import com.farm2table.dto.PriceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "Get optimal price", description = "Calculates the optimal price for a product based on historical prices and demand.")
    @PostMapping("/price")
    public double getOptimalPrice(@RequestBody PriceRequest request) {
        return pricingService.calculateOptimalPrice(request.getHistoricalPrices(), request.getDemand());
    }

    @Operation(summary = "Get product recommendations", description = "Returns product recommendations for the user based on their preferences and past behavior.")
    @GetMapping("/recommend/{userId}")
    public List<Long> getRecommendations(@Parameter(description = "User ID to fetch recommendations for") @PathVariable Long userId) {
        return recommendationService.getRecommendedProducts(userId, 5);
    }

    @Operation(summary = "Check for fraud", description = "Checks if a transaction is fraudulent based on historical transaction data.")
    @PostMapping("/fraud")
    public boolean checkFraud(@RequestBody FraudRequest request) {
        return fraudService.isTransactionFraudulent(request.getTransactionAmounts(), request.getNewTransaction());
    }
}