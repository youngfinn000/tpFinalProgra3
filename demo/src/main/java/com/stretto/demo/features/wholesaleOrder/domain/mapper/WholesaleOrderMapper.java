package com.stretto.demo.features.wholesaleOrder.domain.mapper;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;
import org.springframework.stereotype.Component;

@Component
public class WholesaleOrderMapper {

    public static WholesaleOrderEntity toEntity ( RequestBudgetEntity  requestBudget, WholesaleCustomerEntity  customer) {
        return WholesaleOrderEntity.builder()
                .advancePayment(requestBudget.isAdvancePayment())
                .advancePaymentAmount(null) //se registra cuando se crea
                .deliveryDate(null)
                .discount(null)
                .active(true)
                .requestBudget(requestBudget)
                .wholesaleCustomer(customer)
                .build();
    }
    public static WholesaleOrderResponse toResponse (WholesaleOrderEntity entity) {
        return WholesaleOrderResponse.builder()
                .id(entity.getId())
                .advancePayment(entity.isAdvancePayment())
                .advancePaymentAmount(entity.getAdvancePaymentAmount())
                .deliveryDate(entity.getDeliveryDate())
                .discount(entity.getDiscount())
                .requestBudgetId(entity.getRequestBudget().getId())
                .wholesaleCustomerId(entity.getWholesaleCustomer().getId())
                .wholesaleCustomerName(entity.getWholesaleCustomer().getCompanyName())
                .build();
    }
}
