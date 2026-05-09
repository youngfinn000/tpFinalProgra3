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
    private final WholesaleOrderRepository wholesaleOrderRepository;

    @PostMapping
    public ResponseEntity<WholesaleOrderResponse> createWholesaleOrder(@Valid@RequestBody WholesaleOrderRequest request){
        WholesaleOrderResponse response = wholesaleOrderService.createWholesaleOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WholesaleOrderResponse>> getallWholesaleOrder(){
        return ResponseEntity.ok(wholesaleOrderService.getallWholesaleOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WholesaleOrderResponse> getWholesaleOrderById(@PathVariable Long id){
        return ResponseEntity.ok(wholesaleOrderService.getWholesaleOrderById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteWholesaleOrder(Long id){
        WholesaleOrderEntity entity = wholesaleOrderRepository.findById(id).orElseThrow(()->new RuntimeException("Wholesale order not found"));
        entity.setActive(false);
        wholesaleOrderRepository.save(entity);
    }

}
