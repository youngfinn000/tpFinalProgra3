package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;

import java.math.BigDecimal;
import java.util.List;

public interface WholesaleOrderService {
        ///admin

        // Llamado internamente desde RequestBudgetService al aceptar
        WholesaleOrderResponse  createFromRequestBudget(Long requestId);
        // Listado de todos los pedidos activos
        List<WholesaleOrderResponse> getallWholesaleOrders();
        // Ficha completa
        WholesaleOrderResponse getWholesaleOrderById(Long id);
        // Baja lógica
        void deleteWholesaleOrder(Long id);
        // Admin actualiza deliveryDate y/o discount
        WholesaleOrderResponse updateWholesaleOrder(Long id,WholesaleOrderRequest wholesaleOrderRequest);


        ///wholesalecustomer

        // registrar anticipo
        WholesaleOrderResponse registerAdvancePayment (Long wholesaleOrderId, Long customerId, BigDecimal amount);
        // ver sus pedidos activos
        List<WholesaleOrderResponse> getWholesaleOrdersByCustomerId(Long customerId);
}
