package com.stretto.demo.features.order;


import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTOResponse> create (@Valid @RequestBody OrderDTORequest request)
    {
        OrderDTOResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTOResponse>> findAll()
    {
        List<OrderDTOResponse> response = orderService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTOResponse> findById (@PathVariable Long id)
    {
        OrderDTOResponse response = orderService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<OrderDTOResponse> updateState (@PathVariable Long id,
                                                         @RequestParam StateOrderEnum state)
    {
        OrderDTOResponse response = orderService.updateState(id, state);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel (@PathVariable Long id)
    {
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTOResponse>> findByUser(@PathVariable Long userId)
    {
        List<OrderDTOResponse> response = orderService.findByUser(userId);
        return ResponseEntity.ok(response);

    }

}
