package com.stretto.demo.features.ingredient.domain.mapper;

import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.ingredient.domain.dto.IngredientDTORequest;
import com.stretto.demo.features.ingredient.domain.dto.IngredientDTOResponse;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.stock.domain.StockEntity;

public class IngredientMapper {

    public static IngredientDTOResponse toResponse(IngredientEntity entity)
    {
        return IngredientDTOResponse.builder()
                .id(entity.getId())
                .requiredAmount(entity.getRequiredAmount())
                .unit(entity.getUnit())
                .stock(stockSumarry(entity.getStock()))
                .recipe(recipeSummary(entity.getRecipe()))
                .build();

    }

    public static IngredientEntity toEntity(IngredientDTORequest request, StockEntity stockEntity, RecipeEntity recipeEntity)
    {
        return IngredientEntity.builder()
                .requiredAmount(request.getRequiredAmount())
                .unit(request.getUnit())
                .stock(stockEntity)
                .recipe(recipeEntity)
                .build();
    }

    public static IngredientDTOResponse.StockSummary stockSumarry(StockEntity entity)
    {
        return IngredientDTOResponse.StockSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static IngredientDTOResponse.RecipeSummary recipeSummary(RecipeEntity entity)
    {
        return IngredientDTOResponse.RecipeSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
