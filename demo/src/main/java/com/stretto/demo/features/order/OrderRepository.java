package com.stretto.demo.features.order;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity , Long> {

    OrderEntity findById(WholesaleOrderRequest request);
}
