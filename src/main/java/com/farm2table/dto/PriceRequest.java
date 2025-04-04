package com.farm2table.dto;

import lombok.Data;
import java.util.List;

@Data
public class PriceRequest {
    private List<Double> historicalPrices;
    private List<Integer> demand;

    public boolean isValid() {
        return historicalPrices != null && !historicalPrices.isEmpty() &&
                demand != null && !demand.isEmpty();
    }
}