package com.stretto.demo.features.order.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyReportDTO {

    private Integer year;
    private Integer month;
    private Integer totalOrders;
    private Integer cancelleOrders;
    private BigDecimal totalRevenue;
}
