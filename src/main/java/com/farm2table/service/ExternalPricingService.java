package com.farm2table.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalPricingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${app.external.api.openfoodfacts.url:https://world.openfoodfacts.org/api/v0}")
    private String openFoodFactsBaseUrl;
    
    @Value("${app.external.api.usda.url:https://api.nal.usda.gov/fdc/v1}")
    private String usdaBaseUrl;
    
    @Value("${app.external.api.usda.key:}")
    private String usdaApiKey;

    public ExternalPricingService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Search for products by name and retrieve their prices
     * 
     * @param productName The name of the product to search for
     * @param limit Maximum number of results to return
     * @return List of products with their prices
     */
    @Cacheable(value = "productPrices", key = "#productName")
    public List<Map<String, Object>> searchProducts(String productName, int limit) {
        try {
            String url = openFoodFactsBaseUrl + "/search?search_terms=" + productName + "&page_size=" + limit;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode products = root.path("products");
                
                List<Map<String, Object>> results = new ArrayList<>();
                
                if (products.isArray()) {
                    for (JsonNode product : products) {
                        Map<String, Object> productData = new HashMap<>();
                        
                        productData.put("id", product.path("_id").asText());
                        productData.put("name", product.path("product_name").asText());
                        productData.put("brand", product.path("brands").asText());
                        
                        // Try to get price from different possible locations
                        JsonNode priceNode = product.path("prices");
                        if (!priceNode.isMissingNode()) {
                            productData.put("price", priceNode.path("price").asDouble());
                            productData.put("currency", priceNode.path("currency").asText());
                        } else {
                            // Fallback to other price fields
                            JsonNode priceField = product.path("price");
                            if (!priceField.isMissingNode()) {
                                productData.put("price", priceField.asDouble());
                                productData.put("currency", "USD");
                            }
                        }
                        
                        results.add(productData);
                    }
                }
                
                return results;
            }
        } catch (Exception e) {
            log.error("Error fetching product prices from Open Food Facts API", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get product details by barcode
     * 
     * @param barcode The product barcode
     * @return Product details including price
     */
    @Cacheable(value = "productDetails", key = "#barcode")
    public Map<String, Object> getProductByBarcode(String barcode) {
        try {
            String url = openFoodFactsBaseUrl + "/product/" + barcode + ".json";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode product = root.path("product");
                
                if (!product.isMissingNode()) {
                    Map<String, Object> productData = new HashMap<>();
                    
                    productData.put("id", product.path("_id").asText());
                    productData.put("name", product.path("product_name").asText());
                    productData.put("brand", product.path("brands").asText());
                    
                    // Try to get price from different possible locations
                    JsonNode priceNode = product.path("prices");
                    if (!priceNode.isMissingNode()) {
                        productData.put("price", priceNode.path("price").asDouble());
                        productData.put("currency", priceNode.path("currency").asText());
                    } else {
                        // Fallback to other price fields
                        JsonNode priceField = product.path("price");
                        if (!priceField.isMissingNode()) {
                            productData.put("price", priceField.asDouble());
                            productData.put("currency", "USD");
                        }
                    }
                    
                    return productData;
                }
            }
        } catch (Exception e) {
            log.error("Error fetching product details from Open Food Facts API", e);
        }
        
        return new HashMap<>();
    }
    
    /**
     * Get USDA food data by name
     * 
     * @param foodName The name of the food
     * @return List of foods with their data
     */
    @Cacheable(value = "usdaFoods", key = "#foodName")
    public List<Map<String, Object>> searchUSDAFoods(String foodName) {
        if (usdaApiKey == null || usdaApiKey.isEmpty()) {
            log.warn("USDA API key not configured. Skipping USDA API call.");
            return new ArrayList<>();
        }
        
        try {
            String url = usdaBaseUrl + "/foods/search?api_key=" + usdaApiKey + "&query=" + foodName;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode foods = root.path("foods");
                
                List<Map<String, Object>> results = new ArrayList<>();
                
                if (foods.isArray()) {
                    for (JsonNode food : foods) {
                        Map<String, Object> foodData = new HashMap<>();
                        
                        foodData.put("id", food.path("fdcId").asText());
                        foodData.put("name", food.path("description").asText());
                        foodData.put("brand", food.path("brandOwner").asText());
                        
                        // USDA doesn't provide prices directly, but we can use this data
                        // to enrich our product information
                        
                        results.add(foodData);
                    }
                }
                
                return results;
            }
        } catch (Exception e) {
            log.error("Error fetching food data from USDA API", e);
        }
        
        return new ArrayList<>();
    }
} 