package com.farm2table.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class PriceRequest {
    private List<Double> historicalPrices;
    private List<Integer> demand;

    public boolean isValid() {
        return historicalPrices != null && !historicalPrices.isEmpty() &&
                demand != null && !demand.isEmpty();
    }
}