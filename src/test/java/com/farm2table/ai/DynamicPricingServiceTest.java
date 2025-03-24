package com.farm2table.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class DynamicPricingServiceTest {

    private DynamicPricingService dynamicPricingService;

    @BeforeEach
    void setUp() {
        dynamicPricingService = new DynamicPricingService();
    }

    @Test
    void testCalculateOptimalPrice() {
        List<Double> historicalPrices = Arrays.asList(10.0, 12.0, 14.0);
        List<Integer> demand = Arrays.asList(50, 100, 75);

        double optimalPrice = dynamicPricingService.calculateOptimalPrice(historicalPrices, demand);

        assertTrue(optimalPrice > 0, "Optimal price should be greater than 0");
    }

    @Test
    void testCalculateOptimalPriceWithEmptyHistoricalPrices() {
        List<Double> historicalPrices = Arrays.asList();
        List<Integer> demand = Arrays.asList(50, 100, 75);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            dynamicPricingService.calculateOptimalPrice(historicalPrices, demand);
        });

        assertEquals("Insufficient data for price calculation.", thrown.getMessage());
    }

    @Test
    void testCalculateOptimalPriceWithEmptyDemand() {
        List<Double> historicalPrices = Arrays.asList(10.0, 12.0, 14.0);
        List<Integer> demand = Arrays.asList();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            dynamicPricingService.calculateOptimalPrice(historicalPrices, demand);
        });

        assertEquals("Insufficient data for price calculation.", thrown.getMessage());
    }
}