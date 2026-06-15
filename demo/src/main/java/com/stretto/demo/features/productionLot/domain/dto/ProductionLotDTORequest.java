package com.stretto.demo.features.productionLot.domain.dto;

import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionLotDTORequest {
    @NotNull(message = "recipe id is required")
    private Long recipeId;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Amount produced is required")
    private Double amountProduced;

    @NotNull(message = "Status is required")
    private StatusLotEnum status;

    @NotNull(message = "Performance PCT is required")
    private Double performancePCT;

    @NotNull(message = "Production date is required")
    private LocalDate productionDate;
}
