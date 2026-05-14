package com.stretto.demo.features.productionLot.domain.mapper;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.recipe.domain.RecipeEntity;

public class ProductionLotMapper {

    public static ProductionLotDTOResponse toResponse(ProductionLotEntity entity)
    {
        return ProductionLotDTOResponse.builder()
                .id(entity.getId())
                .recipe(recipeSummary(entity.getRecipe()))
                .internalUser(userSummary(entity.getInternalUser()))
                .amountProduced(entity.getAmountProduced())
                .status(entity.getStatus())
                .performancePCT(entity.getPerformancePct())
                .build();
    }

    public static ProductionLotEntity toEntity(ProductionLotDTORequest request, InternalUserEntity internalUserEntity, RecipeEntity recipe)
    {
        return ProductionLotEntity.builder()
                .recipe(recipe)
                .internalUser(internalUserEntity)
                .amountProduced(request.getAmountProduced())
                .status(request.getStatus())
                .performancePct(request.getPerformancePCT())
                .build();
    }

    public static ProductionLotDTOResponse.InternalUserSummary userSummary(InternalUserEntity entity)
    {
        return ProductionLotDTOResponse.InternalUserSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static ProductionLotDTOResponse.RecipeSummary recipeSummary(RecipeEntity entity)
    {
        return ProductionLotDTOResponse.RecipeSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
