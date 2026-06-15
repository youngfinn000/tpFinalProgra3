package com.stretto.demo.features.internalUser;

import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internalUser")
public class InternalUserController {
    private final InternalUserService internalUserService;

    @PostMapping
    public ResponseEntity<InternalUserDTOResponse> create(@Valid @RequestBody InternalUserDTORequest request)
    {
        InternalUserDTOResponse response = internalUserService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InternalUserDTOResponse>> findAll()
    {
        List<InternalUserDTOResponse> responses = internalUserService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternalUserDTOResponse> findById(@PathVariable Long id)
    {
        InternalUserDTOResponse response = internalUserService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternalUserDTOResponse> update(@PathVariable Long id,
                                                           @RequestBody InternalUserDTORequest request){
        InternalUserDTOResponse response = internalUserService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        internalUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<InternalUserDTOResponse> activate(@PathVariable Long id)
    {
        InternalUserDTOResponse response = internalUserService.activate(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/order")
    public ResponseEntity<OrderDTOResponse> createOrder(@PathVariable Long userId, @RequestBody OrderDTORequest request){
        OrderDTOResponse response = internalUserService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{userId}/lot")
    public ResponseEntity<ProductionLotDTOResponse> createLote(@PathVariable Long userId, @RequestBody ProductionLotDTORequest request){
        ProductionLotDTOResponse response = internalUserService.registerLot(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
