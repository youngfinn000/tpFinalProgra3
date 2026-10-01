package com.stretto.demo.features.orderDetail.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTORequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotEmpty
    private List<Long> flavorsId;

    @NotNull(message = "Quantity is required")
    @Positive
    private Integer quantity;
}
