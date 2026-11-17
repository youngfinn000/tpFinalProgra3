package com.stretto.demo.features.productionLot.domain.mapper;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotActivityDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import com.stretto.demo.features.recipe.domain.RecipeEntity;

import java.time.LocalDate;

public class ProductionLotMapper {

    public static ProductionLotDTOResponse toResponse(ProductionLotEntity entity) {
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

    public static ProductionLotEntity toEntity(
            ProductionLotDTORequest request,
            InternalUserEntity internalUserEntity,
            RecipeEntity recipe
    ) {
        Double baseQty = (recipe != null && recipe.getBaseQuantity() != null && recipe.getBaseQuantity() > 0)
                ? recipe.getBaseQuantity()
                : 1.0;

        Double perfPct = request.getPerformancePCT() != null
                ? request.getPerformancePCT()
                : (request.getAmountProduced() / baseQty) * 100.0;

        StatusLotEnum status = request.getStatus() != null
                ? request.getStatus()
                : StatusLotEnum.COMPLETED;

        LocalDate prodDate = request.getProductionDate() != null
                ? request.getProductionDate()
                : LocalDate.now();

        return ProductionLotEntity.builder()
                .recipe(recipe)
                .internalUser(internalUserEntity)
                .amountProduced(request.getAmountProduced())
                .status(status)
                .performancePct(perfPct)
                .productionDate(prodDate)
                .build();
    }

    public static ProductionLotDTOResponse.InternalUserSummary userSummary(InternalUserEntity entity) {
        if (entity == null) {
            return null;
        }
        return ProductionLotDTOResponse.InternalUserSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static ProductionLotDTOResponse.RecipeSummary recipeSummary(RecipeEntity entity) {
        if (entity == null) {
            return null;
        }
        Long flavorId = entity.getFlavor() != null ? entity.getFlavor().getId() : null;
        String flavorName = entity.getFlavor() != null ? entity.getFlavor().getName() : null;

        return ProductionLotDTOResponse.RecipeSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .flavorId(flavorId)
                .flavorName(flavorName)
                .build();
    }

    public static ProductionLotActivityDTOResponse toActivityResponse(ProductionLotEntity entity) {
        RecipeEntity recipe = entity.getRecipe();
        InternalUserEntity user = entity.getInternalUser();
        Long flavorId = (recipe != null && recipe.getFlavor() != null) ? recipe.getFlavor().getId() : null;
        String flavorName = (recipe != null && recipe.getFlavor() != null) ? recipe.getFlavor().getName() : null;

        return ProductionLotActivityDTOResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .amountProduced(entity.getAmountProduced())
                .performancePCT(entity.getPerformancePct())
                .recipe(recipe != null ? ProductionLotActivityDTOResponse.RecipeSummary.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .flavorId(flavorId)
                        .flavorName(flavorName)
                        .build() : null)
                .user(user != null ? ProductionLotActivityDTOResponse.InternalUserSummary.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build() : null)
                .productionDate(entity.getProductionDate())
                .build();
    }
}
