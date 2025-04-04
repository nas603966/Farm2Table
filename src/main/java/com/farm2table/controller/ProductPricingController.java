package com.farm2table.controller;

import com.farm2table.service.ProductPricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
public class ProductPricingController {

    @Autowired
    private ProductPricingService productPricingService;

    @Operation(summary = "Get comprehensive pricing data", description = "Retrieves comprehensive pricing data for a product including external prices and dynamic pricing insights.")
    @GetMapping("/comprehensive/{productName}")
    public ResponseEntity<Map<String, Object>> getComprehensivePricingData(
            @Parameter(description = "Product name") @PathVariable String productName) {
        
        if (productName == null || productName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> pricingData = productPricingService.getComprehensivePricingData(productName);
        
        if (pricingData.containsKey("message")) {
            return ResponseEntity.ok(pricingData);
        }
        
        return ResponseEntity.ok(pricingData);
    }

    @Operation(summary = "Get pricing recommendations", description = "Retrieves pricing recommendations for a product based on market data and dynamic pricing analysis.")
    @GetMapping("/recommendations/{productName}")
    public ResponseEntity<Map<String, Object>> getPricingRecommendations(
            @Parameter(description = "Product name") @PathVariable String productName) {
        
        if (productName == null || productName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> recommendations = productPricingService.getPricingRecommendations(productName);
        
        if (recommendations.containsKey("message")) {
            return ResponseEntity.ok(recommendations);
        }
        
        return ResponseEntity.ok(recommendations);
    }
} 