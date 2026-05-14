package com.stretto.demo.features.recipe.domain.mapper;

import com.stretto.demo.features.ingredient.IngredientEntity;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;

import java.util.List;

public class RecipeMapper {

    public static RecipeDTOResponse toResponse(RecipeEntity entity)
    {
        return RecipeDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .baseQuantity(entity.getBaseQuantity())
                .unitBase(entity.getUnitBase())
                .product(productSummary(entity.getProduct()))
                .productionLot(productionLotSummary(entity.getProductionLot()))
                .ingredients(entity.getIngredients().stream()
                        .map(RecipeMapper::ingredientSummary)
                        .toList())
                .subRecipes(entity.getSubRecipes().stream()
                        .map(RecipeMapper::recipeSummary)
                        .toList())
                .build();
    }

    public static RecipeEntity toEntity(RecipeDTORequest request, ProductEntity productEntity, ProductionLotEntity productionLotEntity, List<IngredientEntity> ingredientEntity, List<RecipeEntity> subRecipes)
    {
        return RecipeEntity.builder()
                .name(request.getName())
                .baseQuantity(request.getBaseQuantity())
                .unitBase(request.getUnitBase())
                .product(productEntity)
                .productionLot(productionLotEntity)
                .ingredients(ingredientEntity)
                .subRecipes(subRecipes)
                .build();
    }

    public static RecipeDTOResponse.ProductionLotSummary productionLotSummary(ProductionLotEntity entity)
    {
        return RecipeDTOResponse.ProductionLotSummary.builder()
                .id(entity.getId())
                .build();
    }
    public static RecipeDTOResponse.ProductSummary productSummary(ProductEntity entity)
    {
        return RecipeDTOResponse.ProductSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static RecipeDTOResponse.IngredientSummary ingredientSummary(IngredientEntity entity)
    {
        return RecipeDTOResponse.IngredientSummary.builder()
                .id(entity.getId())
                .build();
    }


    public static RecipeDTOResponse.RecipeSummary recipeSummary(RecipeEntity entity) {
        return RecipeDTOResponse.RecipeSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
