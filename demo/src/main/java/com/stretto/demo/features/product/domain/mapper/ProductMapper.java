package com.stretto.demo.features.product.domain.mapper;

import com.stretto.demo.features.flavors.domain.mapper.FlavorsMapper;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import com.stretto.demo.features.stock.domain.StockEntity;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    public ProductDTOResponse toResponse(ProductEntity entity)
    {
        return ProductDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .active(entity.isActive())
                .maxFlavors(entity.getMaxFlavors())
                .stock(entity.getStock())
                .build();
    }

    public ProductEntity toEntity (ProductDTORequest request)
    {
        return ProductEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .maxFlavors(request.getMaxFlavors())
                .stock(request.getStock())
                .build();
    }
}
