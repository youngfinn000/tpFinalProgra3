package com.stretto.demo.features.productionLot.domain.dto;

import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionLotDTOResponse {
    private Long id;
    private RecipeSummary recipe;
    private InternalUserSummary internalUser;
    private Double amountProduced;
    private StatusLotEnum status;
    private Double performancePCT;
    private LocalDate productionDate;

    @Getter
    @Builder
    public static class RecipeSummary{
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    public static class InternalUserSummary{
        private Long id;
        private String name;
    }
}
