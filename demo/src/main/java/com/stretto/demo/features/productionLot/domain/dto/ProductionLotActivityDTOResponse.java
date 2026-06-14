package com.stretto.demo.features.productionLot.domain.dto;

import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLotActivityDTOResponse {
    private Long id;
    private StatusLotEnum status;
    private Double amountProduced;
    private Double performancePCT;
    private RecipeSummary recipe;
    private InternalUserSummary user;
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
