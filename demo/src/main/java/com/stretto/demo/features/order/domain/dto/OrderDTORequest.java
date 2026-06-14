package com.stretto.demo.features.order.domain.dto;

import com.stretto.demo.features.order.domain.enums.PaymentMethodEnum;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTORequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTORequest {

    @NotNull(message = "Sale channel is required")
    private SaleChannelEnum saleChannelEnum;

    @NotNull (message = "Payment method is require")
    private PaymentMethodEnum paymentMethodEnum;

    @NotNull(message = "User id is required")
    private Long userID;

    @NotEmpty
    private List<OrderDetailDTORequest> details;
}
