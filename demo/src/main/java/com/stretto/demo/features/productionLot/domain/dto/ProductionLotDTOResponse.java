package com.stretto.demo.features.productionLot.domain.dto;

import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import lombok.*;

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
