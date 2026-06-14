package com.stretto.demo.features.order.domain.mapper;


import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import com.stretto.demo.features.orderDetail.domain.mapper.OrderDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderDetailMapper orderDetailMapper;

    public OrderDTOResponse toResponse (OrderEntity entity) {
        return OrderDTOResponse.builder()
                .id(entity.getId())
                .saleChannelEnum(entity.getSaleChannelEnum())
                .stateOrderEnum(entity.getStateOrderEnum())
                .paymentMethodEnum(entity.getPaymentMethodEnum())
                .total(entity.getTotal())
                .date(entity.getDate())
                .username(entity.getInternalUser().getName())
                .details(
                        entity.getOrderDetails()
                        .stream()
                        .map(orderDetailMapper::toResponse).toList()
                )
               .build();
    }

    public OrderEntity toEntity (OrderDTORequest request,
                                        InternalUserEntity user)
    {
       return OrderEntity.builder()
               .internalUser(user)
               .saleChannelEnum(request.getSaleChannelEnum())
               .paymentMethodEnum(request.getPaymentMethodEnum())
               .stateOrderEnum(StateOrderEnum.PENDING)
               .date(LocalDateTime.now())
               .build();
    }
}
