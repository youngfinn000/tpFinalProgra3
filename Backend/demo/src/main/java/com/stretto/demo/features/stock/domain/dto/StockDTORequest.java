package com.stretto.demo.features.stock.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import com.stretto.demo.features.stock.domain.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTORequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Current Stock is required")
    private Double currentStock;

    @NotNull(message = "Minimun stock is required")
    private Double minimumStock;

    @NotNull(message = "Unit measurement is required")
    private UnitMeasurementEnum unitMeasurement;

    @NotNull(message = "Status is required")
    private StatusEnum status;
}
