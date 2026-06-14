package com.stretto.demo.features.order.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyReportDTO {

    private LocalDate date;
    private Integer totalOrders;
    private Integer cancellOrders;
    private BigDecimal totalRevenue;
}
