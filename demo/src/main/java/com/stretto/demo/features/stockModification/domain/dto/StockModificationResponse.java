package com.stretto.demo.features.stockModification.domain.dto;

import com.stretto.demo.features.stockModification.domain.enums.AdjustmentTypeEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockModificationResponse {
    private Long id;
    private AdjustmentTypeEnum adjustmentType;
    private Double amount;
    private String motive;
    private LocalDate modificationDate;
    private UserSummary user;
    private StockSummary stock;

    @Getter
    @Builder
    public static class UserSummary{
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    public static class StockSummary{
        private Long id;
        private String name;
    }
}

