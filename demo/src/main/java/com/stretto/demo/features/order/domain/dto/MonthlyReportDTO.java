package com.stretto.demo.features.order.domain.dto;

import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

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
    private Map<SaleChannelEnum, BigDecimal> revenueByChannel;
}
