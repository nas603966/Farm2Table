package com.farm2table.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class RecommendationServiceTest {

    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationService();
    }

    @Test
    void testGetRecommendedProducts() {
        // Mock user purchase history
        Long userId = 1L;
        recommendationService.updateUserPurchaseHistory(userId, 101L);
        recommendationService.updateUserPurchaseHistory(userId, 102L);

        // Mock product similarities
        recommendationService.updateProductSimilarity(101L, Arrays.asList(103L, 104L));
        recommendationService.updateProductSimilarity(102L, Arrays.asList(105L, 104L));

        List<Long> recommendations = recommendationService.getRecommendedProducts(userId, 3);

        assertEquals(3, recommendations.size(), "Should return up to 3 recommendations");
        assertTrue(recommendations.contains(103L), "Should recommend product 103");
        assertTrue(recommendations.contains(104L), "Should recommend product 104");
        assertTrue(recommendations.contains(105L), "Should recommend product 105");
    }

    @Test
    void testGetRecommendedProductsWithNoHistory() {
        Long userId = 2L; // No purchase history for this user

        List<Long> recommendations = recommendationService.getRecommendedProducts(userId, 3);

        assertEquals(3, recommendations.size(), "Should return up to 3 popular products");
    }

    @Test
    void testUpdateUserPurchaseHistory() {
        Long userId = 3L;
        recommendationService.updateUserPurchaseHistory(userId, 106L);
        recommendationService.updateUserPurchaseHistory(userId, 107L);

        List<Long> history = recommendationService.getUserPurchaseHistory(userId);

        assertEquals(2, history.size(), "User purchase history should contain 2 products");
        assertTrue(history.contains(106L), "Purchase history should contain product 106");
        assertTrue(history.contains(107L), "Purchase history should contain product 107");
    }
}