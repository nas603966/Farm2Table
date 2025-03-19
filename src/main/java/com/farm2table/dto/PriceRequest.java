package com.farm2table.dto;

import java.util.List;

public class PriceRequest {
    private List<Double> historicalPrices;
    private List<Integer> demand;

    public List<Double> getHistoricalPrices() {
        return historicalPrices;
    }

    public void setHistoricalPrices(List<Double> historicalPrices) {
        this.historicalPrices = historicalPrices;
    }

    public List<Integer> getDemand() {
        return demand;
    }

    public void setDemand(List<Integer> demand) {
        this.demand = demand;
    }
}
