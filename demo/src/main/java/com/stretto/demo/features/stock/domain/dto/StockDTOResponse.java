package com.stretto.demo.features.stock.domain.dto;

import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import com.stretto.demo.features.stock.domain.enums.StatusEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTOResponse {
    private Long id;
    private String name;
    private Double currentStock;
    private Double minimumStock;
    private UnitMeasurementEnum unit;
    private StatusEnum status;
}
