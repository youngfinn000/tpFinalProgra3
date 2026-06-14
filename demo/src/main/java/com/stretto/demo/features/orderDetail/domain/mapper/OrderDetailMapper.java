package com.stretto.demo.features.orderDetail.domain.mapper;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.orderDetail.domain.OrderDetailEntity;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTORequest;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTOResponse;
import com.stretto.demo.features.product.domain.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDetailMapper {

    public OrderDetailDTOResponse toResponse (OrderDetailEntity entity)
    {
        return OrderDetailDTOResponse.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getName())
                .flavorNames(entity.getFlavors().stream()
                        .map(FlavorsEntity::getName)
                        .toList())
                .build();
    }

    public OrderDetailEntity toEntity (OrderDetailDTORequest dto,
                                       ProductEntity product,
                                       List<FlavorsEntity> flavors)
    {
        return OrderDetailEntity.builder()
                .quantity(dto.getQuantity())
                .unitPrice(product.getPrice())
                .flavors(flavors)
                .product(product)
                .build();
    }
}
