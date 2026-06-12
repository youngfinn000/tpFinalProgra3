package com.stretto.demo.features.requestBudget.domain.mapper;

import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;

public class RequestBudgetMapper {



    public static RequestBudgetDtoResponse toResponse (RequestBudgetEntity entity){

        return RequestBudgetDtoResponse.builder()
                .id(entity.getId())
                .quantity(entity.getQuantityKg())
                .requestDateTime(entity.getRequestDateTime())
                .budget(entity.getBudget())
                .advancePayment(entity.isAdvancePayment())
                .stateRequestEnum(entity.getStateRequestEnum())
                .customerName(entity.getCustomer().getContactName())
                .customerId(entity.getCustomer().getId())
                .flavors(entity.getListflavors())
                .build()
                ;
    }

    public static RequestBudgetEntity toEntity(RequestBudgetDtoRequest request){

        return RequestBudgetEntity.builder()
                .quantityKg(request.getQuantity())
                .requestDateTime(request.getRequestDatetime())
                .advancePayment(request.isAdvancePayment())
                .customer(request.getCustomer())
                .listflavors(request.getFlavors())
                .build();
    }

}
