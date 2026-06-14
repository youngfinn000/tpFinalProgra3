package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wholesale-orders")
@RequiredArgsConstructor
public class WholesaleOrderController {

    private final WholesaleOrderService wholesaleOrderService;




    @GetMapping
    public ResponseEntity<List<WholesaleOrderResponse>> getallWholesaleOrder(){
        return ResponseEntity.ok(wholesaleOrderService.getallWholesaleOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WholesaleOrderResponse> getWholesaleOrderById(@PathVariable Long id){
        return ResponseEntity.ok(wholesaleOrderService.getWholesaleOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        wholesaleOrderService.deleteWholesaleOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customer_id}")
    public ResponseEntity<List<WholesaleOrderResponse>> getByCustomer(@PathVariable Long customer_id){
        return ResponseEntity.ok(wholesaleOrderService.getOrdersByCustomer(customer_id));
    }

    @PatchMapping("/{id}/advance-payment")
    public ResponseEntity<WholesaleOrderResponse> registerAdvancePayment(@PathVariable Long id, @RequestParam Long customerId){
        return ResponseEntity.ok(wholesaleOrderService.registerAdvancePayment(id,customerId));
    }


}
