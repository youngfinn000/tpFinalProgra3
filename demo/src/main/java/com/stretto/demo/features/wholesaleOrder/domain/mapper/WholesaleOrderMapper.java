package com.stretto.demo.features.wholesaleOrder.domain.mapper;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;

public class WholesaleOrderMapper {

    public static WholesaleOrderEntity toEntity (WholesaleOrderRequest request, OrderEntity order, RequestBudgetEntity  requestBudget, WholesaleCustomerEntity  customer) {
        return WholesaleOrderEntity.builder()
                .advancePayment(request.getAdvancePaymentStatus())
                .deliveryDate(request.getDeliveryDate())
                .discount(request.getDiscount())
                .order(order)
                .requestBudget(requestBudget)
                .wholesaleCustomer(customer)
                .build();
    }
    public static WholesaleOrderResponse toResponse (WholesaleOrderEntity entity) {
        return WholesaleOrderResponse.builder()
                .id(entity.getId())
                .advancePayment(entity.isAdvancePayment())
                .deliveryDate(entity.getDeliveryDate())
                .discount(entity.getDiscount())
                .orderId(entity.getOrder().getId())
                .requestBudgetId(entity.getRequestBudget().getId())
                .wholesaleCustomerId(entity.getWholesaleCustomer().getId())
                .wholesaleCustomerName(entity.getWholesaleCustomer().getCompanyName())
                .build();
    }
}
