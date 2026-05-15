package com.stretto.demo.features.ingredient.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDTORequest {
    @NotNull(message = "Required amount is required")
    private Double requiredAmount;

    @NotNull(message = "Unit is required")
    private UnitMeasurementEnum unit;

    @NotNull (message = "Recipe Id is required")
    private Long recipeId;

    @NotNull(message = "Stock id is required")
    private Long stockId;
}
