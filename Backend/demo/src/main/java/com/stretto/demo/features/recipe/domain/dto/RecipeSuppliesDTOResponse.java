package com.stretto.demo.features.recipe.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSuppliesDTOResponse {
    private Long id;
    private Long stockId;
    private String ingredientName;
    private Double baseQuantity;
    private Double requiredQuantity;
    private UnitMeasurementEnum unit;
}
