package com.stretto.demo.features.order.domain.dto;

import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

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
    private Map<SaleChannelEnum, BigDecimal> revenueByChannel;
}
