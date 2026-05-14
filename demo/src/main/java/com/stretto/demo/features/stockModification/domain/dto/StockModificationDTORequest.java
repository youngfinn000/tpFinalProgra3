package com.stretto.demo.features.stockModification.domain.dto;

import com.stretto.demo.features.stockModification.domain.enums.AdjustmentTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockModificationDTORequest {
    @NotNull(message = "Adjustment type is required")
    private AdjustmentTypeEnum adjustmentType;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotBlank(message = "Motive is required")
    @Size(max = 150, message = "Motive cannot exceed 150 characters")
    private String motive;

    @NotNull(message = "Stock ID is required")
    private Long stockId;

    @NotNull(message = "User Id is required")
    private Long userId;
}
