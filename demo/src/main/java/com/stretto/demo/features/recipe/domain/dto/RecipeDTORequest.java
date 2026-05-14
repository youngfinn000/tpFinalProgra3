package com.stretto.demo.features.recipe.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDTORequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Base Quantity is required")
    private Double baseQuantity;

    @NotNull(message = "Unit Base is required")
    private UnitMeasurementEnum unitBase;

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Production Lot Id is required")
    private Long productionLotId;

    @NotNull(message = "Ingredients are required")
    private List<Long> ingredientIds;

    private List<Long> subRecipeIds;
}
