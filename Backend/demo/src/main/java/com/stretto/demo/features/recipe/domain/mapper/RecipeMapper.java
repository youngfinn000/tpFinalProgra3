package com.stretto.demo.features.recipe.domain.mapper;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;
import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;

import java.util.Collections;
import java.util.List;

public class RecipeMapper {

    public static RecipeDTOResponse toResponse(RecipeEntity entity) {
        List<IngredientEntity> ingredients = entity.getIngredients() != null
                ? entity.getIngredients()
                : Collections.emptyList();
        List<RecipeEntity> subRecipes = entity.getSubRecipes() != null
                ? entity.getSubRecipes()
                : Collections.emptyList();

        return RecipeDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .baseQuantity(entity.getBaseQuantity())
                .unitBase(entity.getUnitBase() != null ? entity.getUnitBase() : UnitMeasurementEnum.KG)
                .flavor(flavorSummary(entity.getFlavor()))
                .product(productSummary(entity.getProduct()))
                .productionLot(productionLotSummary(entity.getProductionLot()))
                .ingredients(ingredients.stream()
                        .map(RecipeMapper::ingredientSummary)
                        .toList())
                .subRecipes(subRecipes.stream()
                        .map(RecipeMapper::recipeSummary)
                        .toList())
                .build();
    }

    public static RecipeEntity toEntity(
            RecipeDTORequest request,
            FlavorsEntity flavorEntity,
            ProductEntity productEntity,
            ProductionLotEntity productionLotEntity,
            List<IngredientEntity> ingredientEntities,
            List<RecipeEntity> subRecipes
    ) {
        String resolvedName = (request.getName() != null && !request.getName().isBlank())
                ? request.getName().trim()
                : (flavorEntity != null ? flavorEntity.getName() : "Receta");

        return RecipeEntity.builder()
                .name(resolvedName)
                .baseQuantity(request.getBaseQuantity())
                .unitBase(request.getUnitBase() != null ? request.getUnitBase() : UnitMeasurementEnum.KG)
                .flavor(flavorEntity)
                .product(productEntity)
                .productionLot(productionLotEntity)
                .ingredients(ingredientEntities)
                .subRecipes(subRecipes)
                .build();
    }

    public static RecipeDTOResponse.FlavorSummary flavorSummary(FlavorsEntity entity) {
        if (entity == null) {
            return null;
        }
        return RecipeDTOResponse.FlavorSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static RecipeDTOResponse.ProductionLotSummary productionLotSummary(ProductionLotEntity entity) {
        if (entity == null) {
            return null;
        }
        return RecipeDTOResponse.ProductionLotSummary.builder()
                .id(entity.getId())
                .build();
    }

    public static RecipeDTOResponse.ProductSummary productSummary(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return RecipeDTOResponse.ProductSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static RecipeDTOResponse.IngredientSummary ingredientSummary(IngredientEntity entity) {
        if (entity == null) {
            return null;
        }
        Long stockId = entity.getStock() != null ? entity.getStock().getId() : null;
        String stockName = entity.getStock() != null ? entity.getStock().getName() : null;
        UnitMeasurementEnum unit = entity.getUnit() != null
                ? entity.getUnit()
                : (entity.getStock() != null ? entity.getStock().getUnit() : null);

        return RecipeDTOResponse.IngredientSummary.builder()
                .id(entity.getId())
                .stockId(stockId)
                .stockName(stockName)
                .ingredientName(stockName)
                .requiredAmount(entity.getRequiredAmount())
                .unit(unit)
                .build();
    }

    public static RecipeDTOResponse.RecipeSummary recipeSummary(RecipeEntity entity) {
        if (entity == null) {
            return null;
        }
        return RecipeDTOResponse.RecipeSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
