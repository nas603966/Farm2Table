package com.farm2table.ai;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RecommendationService {

    private final Map<Long, List<Long>> userPurchaseHistory = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> productSimilarity = new ConcurrentHashMap<>();
    private static final int MAX_HISTORY_SIZE = 20;
    private static final double SIMILARITY_WEIGHT = 0.5;

    public List<Long> getRecommendedProducts(Long userId, int limit) {
        List<Long> purchasedProducts = userPurchaseHistory.getOrDefault(userId, new ArrayList<>());

        if (purchasedProducts.isEmpty()) {
            System.out.println("No purchase history found for user: " + userId);
            return getPopularProducts(limit);
        }

        Map<Long, Double> recommendationScores = new HashMap<>();
        for (Long product : purchasedProducts) {
            List<Long> similarProducts = productSimilarity.getOrDefault(product, new ArrayList<>());
            for (Long similarProduct : similarProducts) {
                recommendationScores.put(similarProduct, recommendationScores.getOrDefault(similarProduct, 0.0) + SIMILARITY_WEIGHT);
            }
        }

        return recommendationScores.entrySet().stream()
                .filter(entry -> !purchasedProducts.contains(entry.getKey()))
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void updateUserPurchaseHistory(Long userId, Long productId) {
        List<Long> history = userPurchaseHistory.computeIfAbsent(userId, k -> new ArrayList<>());

        if (history.size() >= MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(productId);
    }

    public void updateProductSimilarity(Long productId, List<Long> similarProducts) {
        productSimilarity.computeIfAbsent(productId, k -> new ArrayList<>()).addAll(similarProducts);
    }

    public List<Long> getUserPurchaseHistory(Long userId) {
        return userPurchaseHistory.getOrDefault(userId, new ArrayList<>());
    }

    private List<Long> getPopularProducts(int limit) {
        List<Long> popularProducts = List.of(101L, 102L, 103L, 104L, 105L);
        return popularProducts.stream().limit(limit).toList();
    }
}