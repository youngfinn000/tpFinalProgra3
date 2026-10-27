package com.stretto.demo.features.wholesaleOrder.domain.mapper;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class WholesaleOrderMapper {

    public static WholesaleOrderEntity toEntity ( RequestBudgetEntity requestBudget, WholesaleCustomerEntity customer) {
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
        Double budget = null;
        Integer quantity = null;
        LocalDate eventDate = null;
        String state = null;
        if (entity.getRequestBudget() != null) {
            budget = entity.getRequestBudget().getBudget();
            quantity = entity.getRequestBudget().getQuantityKg();
            eventDate = entity.getRequestBudget().getEventDate();
            if (entity.getRequestBudget().getStateRequestEnum() != null) {
                state = entity.getRequestBudget().getStateRequestEnum().name();
            }
        }

        return WholesaleOrderResponse.builder()
                .id(entity.getId())
                .advancePayment(entity.isAdvancePayment())
                .advancePaymentAmount(entity.getAdvancePaymentAmount())
                .deliveryDate(entity.getDeliveryDate())
                .discount(entity.getDiscount())
                .requestBudgetId(entity.getRequestBudget() != null ? entity.getRequestBudget().getId() : null)
                .wholesaleCustomerId(entity.getWholesaleCustomer() != null ? entity.getWholesaleCustomer().getId() : null)
                .wholesaleCustomerName(entity.getWholesaleCustomer() != null ? entity.getWholesaleCustomer().getCompanyName() : null)
                .budget(budget)
                .quantity(quantity)
                .eventDate(eventDate)
                .stateRequestEnum(state)
                .build();
    }
}
