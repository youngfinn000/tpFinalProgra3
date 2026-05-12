package com.stretto.demo.features.order.domain.dto;

import com.stretto.demo.features.order.domain.enums.PaymentMethodEnum;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTOResponse {

    private Long id;
    private StateOrderEnum stateOrderEnum;
    private SaleChannelEnum saleChannelEnum;
    private PaymentMethodEnum paymentMethodEnum;
    private BigDecimal total;
    private LocalDateTime date;
    private List<Long> productIds;
    private Long userId;

}
