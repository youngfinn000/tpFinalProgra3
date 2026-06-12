package com.stretto.demo.features.requestBudget.domain.dto;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.requestBudget.domain.enums.StateRequestEnum;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBudgetDtoResponse {
    private Long id;
    private int quantity;
    private LocalDateTime requestDateTime;
    private double budget;
    private boolean advancePayment;
    private StateRequestEnum stateRequestEnum;
    private String customerName;
    private Long customerId;
    private List<FlavorsEntity>  flavors;

}
