package com.farm2table.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PriceRequest {
    private List<Double> historicalPrices;
    private List<Integer> demand;

}