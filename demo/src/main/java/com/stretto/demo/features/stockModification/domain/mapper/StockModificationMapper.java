package com.stretto.demo.features.stockModification.domain.mapper;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import com.stretto.demo.features.stock.StockEntity;
import com.stretto.demo.features.stockModification.domain.StockModificationEntity;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTORequest;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTOResponse;

import java.time.LocalDate;

public class StockModificationMapper {

    public static StockModificationDTOResponse toResponse(StockModificationEntity entity)
    {
        return StockModificationDTOResponse.builder()
                .id(entity.getId())
                .adjustmentType(entity.getAdjustmentType())
                .amount(entity.getAmount())
                .modificationDate(entity.getModificationDate())
                .motive(entity.getMotive())
                .user(userSumarry(entity.getInternalUser()))
                .stock(stockSummary(entity.getStock()))
                .build();

    }

    public static StockModificationEntity toEntity(StockModificationDTORequest request, StockEntity stock, InternalUserEntity user)
    {
        return StockModificationEntity.builder()
                .adjustmentType(request.getAdjustmentType())
                .amount(request.getAmount())
                .modificationDate(LocalDate.now())
                .motive(request.getMotive())
                .stock(stock)
                .internalUser(user)
                .build();
    }

    public static StockModificationDTOResponse.UserSummary userSumarry(InternalUserEntity entity)
    {
        return StockModificationDTOResponse.UserSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();

    }

    public static StockModificationDTOResponse.StockSummary stockSummary(StockEntity entity)
    {
        return StockModificationDTOResponse.StockSummary.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
