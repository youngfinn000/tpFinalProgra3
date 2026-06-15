package com.stretto.demo.features.wholesaleOrder.domain.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WholesaleOrderRequest {

    @NotNull(message = "Advance payment status ir required")
    private Boolean advancePaymentStatus;

    @DecimalMin(value = "0.01",message = "Advance payment amount must be greater than 0")
    private BigDecimal advancePaymentAmount;

    @FutureOrPresent(message = "Delivery date cannot be in the past")
    private LocalDate deliveryDate;

    private BigDecimal discount;

    @NotNull(message = "Order id is required")
    private Long orderId;

    @NotNull(message = "Request budget id is required")
    private Long requestBudgetId;

    @NotNull(message = "Wholesale customer id is required")
    private Long customerId;

}
