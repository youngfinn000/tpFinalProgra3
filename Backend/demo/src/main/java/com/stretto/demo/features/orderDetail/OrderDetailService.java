package com.stretto.demo.features.orderDetail;

import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTORequest;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTOResponse;

import java.util.List;

public interface OrderDetailService {

    OrderDetailDTOResponse create (Long orderId, OrderDetailDTORequest dto);

    List<OrderDetailDTOResponse> getByOrder (Long orderId);

    void delete (Long id);


}
