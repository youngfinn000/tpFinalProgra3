package com.stretto.demo.features.productionLot.domain.dto;

import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionLotDTORequest {
    @NotNull(message = "Recipe id is required")
    private Long recipeId;

    private Long userId;

    @NotNull(message = "Amount produced is required")
    @Positive(message = "Amount produced must be greater than zero")
    private Double amountProduced;

    private StatusLotEnum status;

    private Double performancePCT;

    private LocalDate productionDate;
}
