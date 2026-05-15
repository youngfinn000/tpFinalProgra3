package com.stretto.demo.features.ingredient.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDTOResponse {
    private Long id;
    private Double requiredAmount;
    private UnitMeasurementEnum unit;
    private RecipeSummary recipe;
    private StockSummary stock;

    @Getter
    @Builder
    public static class RecipeSummary
    {
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    public static class StockSummary
    {
        private Long id;
        private String name;
    }
}
