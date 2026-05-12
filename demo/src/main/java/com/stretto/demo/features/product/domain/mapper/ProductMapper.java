package com.stretto.demo.features.product.domain.mapper;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import com.stretto.demo.features.stock.StockEntity;

import java.util.List;

public class ProductMapper {

    public static ProductDTOResponse toResponse (ProductEntity entity)
    {
        return ProductDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .active(entity.isActive())
                .stockId(entity.getStock().getId())
                .flavorIds(entity.getFlavors()
                        .stream()
                        .map(FlavorsEntity::getId)
                        .toList())
                .build();
    }

    public static ProductEntity toEntity (ProductDTORequest request,
                                          StockEntity stock,
                                          List<FlavorsEntity> flavors)
    {
        return ProductEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(stock)
                .flavors(flavors)
                .build();
    }
}
