package com.stretto.demo.features.order;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity , Long> {

    List<OrderEntity> findByInternalUser_Id(Long userId);

    List<OrderEntity> findByStateOrderEnum(StateOrderEnum state);

    List<OrderEntity> findBySaleChannelEnum(SaleChannelEnum saleChannel);

    List<OrderEntity> findByDateBetween(LocalDateTime startDate,
                                        LocalDateTime endDate);
}
