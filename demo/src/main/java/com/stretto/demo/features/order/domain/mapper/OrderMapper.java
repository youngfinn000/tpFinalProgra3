package com.stretto.demo.features.order.domain.mapper;


import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.product.domain.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {

    public static OrderDTOResponse toResponse (OrderEntity entity)
    {
        return OrderDTOResponse.builder()
                .id(entity.getId())
                .stateOrderEnum(entity.getStateOrderEnum())
                .saleChannelEnum(entity.getSaleChannelEnum())
                .paymentMethodEnum(entity.getPaymentMethodEnum())
                .total(entity.getTotal())
                .date(entity.getDate())
                .productIds(
                        entity.getListProduct()
                                .stream()
                                .map(ProductEntity ::getId)
                                .toList()
                )
                .userId(entity.getInternalUser().getId())
                .build();
    }

    public static OrderEntity toEntity (OrderDTORequest request,
                                        InternalUser user,
                                        List<ProductEntity> products)
    {
        return OrderEntity.builder()
                .stateOrderEnum(request.getStateOrderEnum())
                .saleChannelEnum(request.getSaleChannelEnum())
                .paymentMethodEnum(request.getPaymentMethodEnum())
                .internalUser(user)
                .listProduct(products)
                .date(LocalDateTime.now())
                .build();
    }
}
