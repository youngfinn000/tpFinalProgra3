package com.stretto.demo.features.order;

import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;

import java.util.List;

public interface OrderService {

    OrderDTOResponse create (OrderDTORequest request);

    List<OrderDTOResponse> findAll();

    OrderDTOResponse findById (Long id);

    OrderDTOResponse updateState (Long id, StateOrderEnum state);

    void cancel (Long id);

    List<OrderDTOResponse> findByUser (Long userId);
}
