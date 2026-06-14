package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;

import java.util.List;

public interface WholesaleOrderService {

        public WholesaleOrderResponse  createFromRequestBudget(Long requestBudgetId);
        List<WholesaleOrderResponse> getallWholesaleOrders();
        WholesaleOrderResponse getWholesaleOrderById(Long id);
        public void deleteWholesaleOrder(Long id);
        public WholesaleOrderResponse registerAdvancePayment(Long wholesaleOrderId, Long customerId);
        public List<WholesaleOrderResponse> getOrdersByCustomer (Long customerId);

}
