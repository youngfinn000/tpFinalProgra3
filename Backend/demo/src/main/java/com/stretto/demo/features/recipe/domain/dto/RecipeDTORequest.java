package com.stretto.demo.features.recipe.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDTORequest {

    private String name;

    @NotNull(message = "Base Quantity is required")
    @Positive(message = "Base Quantity must be greater than zero")
    private Double baseQuantity;

    private UnitMeasurementEnum unitBase;

    private Long flavorId;

    private Long productId;

    private Long productionLotId;

    private List<RecipeIngredientRequest> ingredients;

    private List<Long> ingredientIds;

    private List<Long> subRecipeIds;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecipeIngredientRequest {
        @NotNull(message = "Stock Id is required")
        private Long stockId;

        @NotNull(message = "Required amount is required")
        @Positive(message = "Required amount must be greater than zero")
        private Double requiredAmount;

        private UnitMeasurementEnum unit;
    }
}
