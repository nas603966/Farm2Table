package com.farm2table.controller;

import com.farm2table.service.ExternalPricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external/pricing")
public class ExternalPricingController {

    @Autowired
    private ExternalPricingService externalPricingService;

    @Operation(summary = "Search for products by name", description = "Searches for products by name and returns their prices from external APIs.")
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchProducts(
            @Parameter(description = "Product name to search for") @RequestParam String productName,
            @Parameter(description = "Maximum number of results to return") @RequestParam(defaultValue = "10") int limit) {
        
        if (productName == null || productName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Map<String, Object>> results = externalPricingService.searchProducts(productName, limit);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get product by barcode", description = "Retrieves product details including price by barcode from external APIs.")
    @GetMapping("/product/{barcode}")
    public ResponseEntity<Map<String, Object>> getProductByBarcode(
            @Parameter(description = "Product barcode") @PathVariable String barcode) {
        
        if (barcode == null || barcode.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> product = externalPricingService.getProductByBarcode(barcode);
        
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Search USDA food database", description = "Searches the USDA food database for food items.")
    @GetMapping("/usda/search")
    public ResponseEntity<List<Map<String, Object>>> searchUSDAFoods(
            @Parameter(description = "Food name to search for") @RequestParam String foodName,
            @Parameter(description = "Maximum number of results to return") @RequestParam(defaultValue = "10") int limit) {
        
        if (foodName == null || foodName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Map<String, Object>> results = externalPricingService.searchUSDAFoods(foodName);
        
        // Limit results if needed
        if (results.size() > limit) {
            results = results.subList(0, limit);
        }
        
        return ResponseEntity.ok(results);
    }
} 