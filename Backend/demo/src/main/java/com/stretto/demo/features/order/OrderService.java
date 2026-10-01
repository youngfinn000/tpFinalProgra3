package com.stretto.demo.features.order;

import com.stretto.demo.features.order.domain.dto.DailyReportDTO;
import com.stretto.demo.features.order.domain.dto.MonthlyReportDTO;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDTOResponse create (OrderDTORequest request);

    List<OrderDTOResponse> findAll();

    OrderDTOResponse findById (Long id);

    OrderDTOResponse updateState (Long id, StateOrderEnum state);

    void cancel (Long id);

    List<OrderDTOResponse> findByUser (Long userId);

    List<OrderDTOResponse> findByState (StateOrderEnum state);

    List<OrderDTOResponse> findBySaleChannel (SaleChannelEnum saleChannel);

    List<OrderDTOResponse> findBetweenDates (LocalDate startDate,
                                             LocalDate endDate);

    DailyReportDTO generateDailyReport();

    MonthlyReportDTO generateMonthlyReport(int year, int month);

}
