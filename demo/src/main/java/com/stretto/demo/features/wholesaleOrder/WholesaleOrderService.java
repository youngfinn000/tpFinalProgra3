package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;

import java.util.List;

public interface WholesaleOrderService {

        WholesaleOrderResponse createWholesaleOrder(WholesaleOrderRequest wholesaleOrderRequest);
        List<WholesaleOrderResponse> getallWholesaleOrders();
        WholesaleOrderResponse getWholesaleOrderById(Long id);
        void deleteWholesaleOrder(Long id);
}
