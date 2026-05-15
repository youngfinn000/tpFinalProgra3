package com.stretto.demo.features.stock.domain.mapper;

import com.stretto.demo.features.stock.domain.StockEntity;
import com.stretto.demo.features.stock.domain.dto.StockDTORequest;
import com.stretto.demo.features.stock.domain.dto.StockDTOResponse;

public class StockMapper {

    public static StockDTOResponse toResponse(StockEntity entity) {

        return StockDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .currentStock(entity.getCurrentStock())
                .minimumStock(entity.getMinimumStock())
                .unit(entity.getUnit())
                .status(entity.getStatus())
                .build();
    }

    public static StockEntity toEntity(StockDTORequest request) {

        return StockEntity.builder()
                .name(request.getName())
                .currentStock(request.getCurrentStock())
                .minimumStock(request.getMinimumStock())
                .unit(request.getUnitMeasurement())
                .status(request.getStatus())
                .build();
    }
}
