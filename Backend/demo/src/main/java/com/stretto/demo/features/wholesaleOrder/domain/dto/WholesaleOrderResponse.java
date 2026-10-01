package com.stretto.demo.features.wholesaleOrder.domain.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WholesaleOrderResponse {

    private Long id;
    private boolean advancePayment;
    private BigDecimal advancePaymentAmount;
    private LocalDate deliveryDate;
    private BigDecimal discount;
    private Long requestBudgetId;
    private Long wholesaleCustomerId;
    private String wholesaleCustomerName;

}
