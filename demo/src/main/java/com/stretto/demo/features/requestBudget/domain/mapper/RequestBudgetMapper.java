package com.stretto.demo.features.requestBudget.domain.mapper;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
import com.stretto.demo.features.requestBudget.domain.enums.StateRequestEnum;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestBudgetMapper {



    public static RequestBudgetDtoResponse toResponse (RequestBudgetEntity entity){

        return RequestBudgetDtoResponse.builder()
                .id(entity.getId())
                .quantity(entity.getQuantityKg())
                .requestDateTime(entity.getRequestDateTime())
                .eventDate(entity.getEventDate())
                .budget(entity.getBudget())
                .advancePayment(entity.isAdvancePayment())
                .stateRequestEnum(entity.getStateRequestEnum())
                .customerName(entity.getCustomer().getContactName())
                .customerId(entity.getCustomer().getId())
                .flavors(entity.getListflavors())
                .build()
                ;
    }

    public static RequestBudgetEntity toEntity(RequestBudgetDtoRequest request,WholesaleCustomerEntity customer, List<FlavorsEntity> flavors){

        return RequestBudgetEntity.builder()
                .quantityKg(request.getQuantity())
                .requestDateTime(request.getRequestDatetime())
                .eventDate(request.getEventDate())
                .advancePayment(request.isAdvancePayment())
                .stateRequestEnum(StateRequestEnum.PENDING)
                .customer(customer)
                .listflavors(flavors)
                .build();
    }

}
