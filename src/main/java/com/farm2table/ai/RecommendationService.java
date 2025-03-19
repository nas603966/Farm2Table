package com.farm2table.ai;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RecommendationService {

    private Map<Long, List<Long>> userPurchaseHistory = new HashMap<>();
    private Map<Long, List<Long>> productSimilarity = new HashMap<>();

    public List<Long> getRecommendedProducts(Long userId, int limit) {
        List<Long> purchasedProducts = userPurchaseHistory.getOrDefault(userId, new ArrayList<>());
        if (purchasedProducts.isEmpty()) {
            System.out.println("No purchase history found for user: " + userId);
            return Collections.emptyList();
        }

        Map<Long, Double> recommendationScores = new HashMap<>();
        for (Long product : purchasedProducts) {
            List<Long> similarProducts = productSimilarity.getOrDefault(product, new ArrayList<>());
            for (Long similarProduct : similarProducts) {
                recommendationScores.put(similarProduct, recommendationScores.getOrDefault(similarProduct, 0.0) + 1.0);
            }
        }

        return recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void updateUserPurchaseHistory(Long userId, Long productId) {
        userPurchaseHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(productId);
    }

    public void updateProductSimilarity(Long productId, List<Long> similarProducts) {
        productSimilarity.put(productId, similarProducts);
    }
}
