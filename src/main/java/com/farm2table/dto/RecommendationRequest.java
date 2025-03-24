package com.farm2table.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationRequest {
    private Long userId;
    private int limit;
}