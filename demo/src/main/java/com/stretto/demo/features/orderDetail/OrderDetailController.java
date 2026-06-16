package com.stretto.demo.features.orderDetail;

import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTORequest;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    //CREAR DETALLE
    @PostMapping("/{orderId}")
    public ResponseEntity<OrderDetailDTOResponse> createOrderDetail(
            @PathVariable Long orderId, @RequestBody OrderDetailDTORequest dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderDetailService.create(orderId, dto));
    }

    //BUSCAR DETALLE POR ID DEL PEDIDO
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTOResponse>> getByOrder(
            @PathVariable Long orderId
    )
    {
        return ResponseEntity.ok(orderDetailService.getByOrder(orderId));
    }

    //ELIMINAR DETALLE POR ID DEL PEDIDO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        orderDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
