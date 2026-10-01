package com.stretto.demo.features.requestBudget.domain.dto;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBudgetDtoRequest {


    @Positive (message = "Quantity must be valid")
    @DecimalMin("0.5")
    private int quantity;

    @NotNull(message = "Time is required")
    @FutureOrPresent(message = "Date must be in the present or future")
    private LocalDateTime requestDatetime;

    private boolean advancePayment;

    @NotNull(message = "Client is obligatory")
    @Positive(message = "ID must be valid")
    private Long customerId;

    @NotEmpty (message = "Minimum one flavor per request")
    private List<Long> flavorsId;

    @NotNull(message = "Date is required")
    @Future(message = "Date must be in the future")
    private LocalDate eventDate;

}
