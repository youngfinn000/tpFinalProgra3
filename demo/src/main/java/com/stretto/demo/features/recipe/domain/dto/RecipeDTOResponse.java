package com.stretto.demo.features.recipe.domain.dto;

import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDTOResponse {
    private Long id;
    private String name;
    private Double baseQuantity;
    private UnitMeasurementEnum unitBase;
    private ProductSummary product;
    private ProductionLotSummary productionLot;
    private List<IngredientSummary> ingredients;
    private List<RecipeSummary> subRecipes;

    @Getter
    @Builder
    public static class ProductionLotSummary{
        private Long id;
    }

    @Getter
    @Builder
    public static class ProductSummary{
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    public static class IngredientSummary{
        private Long id;
    }

    @Getter
    @Builder
    public static class RecipeSummary {
        private Long id;
        private String name;
    }
}
