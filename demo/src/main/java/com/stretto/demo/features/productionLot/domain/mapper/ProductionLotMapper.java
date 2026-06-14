package com.stretto.demo.features.productionLot.domain.mapper;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotActivityDTOResponse;
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
                .productionDate(entity.getProductionDate())
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
                .productionDate(request.getProductionDate())
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
    public static ProductionLotActivityDTOResponse toActivityResponse(ProductionLotEntity entity){

        return ProductionLotActivityDTOResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .amountProduced(entity.getAmountProduced())
                .performancePCT(entity.getPerformancePct())
                .recipe(ProductionLotActivityDTOResponse.RecipeSummary.builder()
                        .id(entity.getRecipe().getId())
                        .name(entity.getRecipe().getName())
                        .build()
                )
                .user(ProductionLotActivityDTOResponse.InternalUserSummary.builder()
                        .id(entity.getInternalUser().getId())
                        .name(entity.getInternalUser().getName())
                        .build()
                )
                .productionDate(entity.getProductionDate())
                .build();
    }
}
