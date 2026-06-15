package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wholesale-orders")
@RequiredArgsConstructor
public class WholesaleOrderController {

    private final WholesaleOrderService wholesaleOrderService;

    @PostMapping("/request-budget/{requestbudgetId}")
    public ResponseEntity<WholesaleOrderResponse> createWholesaleOrder(@PathVariable Long requestId){
        return ResponseEntity.status(HttpStatus.CREATED).body(wholesaleOrderService.createFromRequestBudget(requestId));
    }

    @GetMapping
    public ResponseEntity<List<WholesaleOrderResponse>> getallWholesaleOrder(){
        return ResponseEntity.ok(wholesaleOrderService.getallWholesaleOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WholesaleOrderResponse> getWholesaleOrderById(@PathVariable Long id){
        return ResponseEntity.ok(wholesaleOrderService.getWholesaleOrderById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WholesaleOrderResponse> updateWholesaleOrder(@PathVariable Long id, @Valid @RequestBody WholesaleOrderRequest request){
        return ResponseEntity.ok(wholesaleOrderService.updateWholesaleOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWholesaleOrder(@PathVariable Long id){
        wholesaleOrderService.deleteWholesaleOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/advance-payment")
    public ResponseEntity<WholesaleOrderResponse> registerAdvancePayment(@PathVariable Long orderId, @Valid@RequestParam Long customerId, @Valid@RequestParam BigDecimal amount){
        return ResponseEntity.ok(wholesaleOrderService.registerAdvancePayment(orderId, customerId, amount));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<WholesaleOrderResponse>> getOrdersByCustomerId(@PathVariable Long customerId){
        return ResponseEntity.ok(wholesaleOrderService.getWholesaleOrdersByCustomerId(customerId));
    }


}
