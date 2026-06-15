package com.stretto.demo.features.order;


import com.stretto.demo.features.order.domain.dto.DailyReportDTO;
import com.stretto.demo.features.order.domain.dto.MonthlyReportDTO;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import com.stretto.demo.features.product.ProductService;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    //CREAR PEDIDO
    @PostMapping
    public ResponseEntity<OrderDTOResponse> create (@Valid @RequestBody OrderDTORequest request)
    {
        OrderDTOResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //BUSCAR TODOS LOS PEDIDOS
    @GetMapping
    public ResponseEntity<List<OrderDTOResponse>> findAll()
    {
        List<OrderDTOResponse> response = orderService.findAll();
        return ResponseEntity.ok(response);
    }

    //BUSCAR PEDIDO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTOResponse> findById (@PathVariable Long id)
    {
        OrderDTOResponse response = orderService.findById(id);
        return ResponseEntity.ok(response);
    }

    //ACTUALIZAR ESTADO DEL PEDIDO
    @PatchMapping("/{id}/state")
    public ResponseEntity<OrderDTOResponse> updateState (@PathVariable Long id,
                                                         @RequestParam StateOrderEnum state)
    {
        OrderDTOResponse response = orderService.updateState(id, state);
        return ResponseEntity.ok(response);
    }

    //CANCELAR EL PEDIDO
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel (@PathVariable Long id)
    {
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    //PEDIDOS POR USUARIO
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<OrderDTOResponse>> findByUser(@PathVariable Long userId)
    {
        List<OrderDTOResponse> response = orderService.findByUser(userId);
        return ResponseEntity.ok(response);

    }

    //FILTRAR POR ESTADO DEL PEDIDO
    @GetMapping("/state/{state}")
    public ResponseEntity<List<OrderDTOResponse>> findByState (
            @PathVariable StateOrderEnum state)
    {
        List<OrderDTOResponse> response = orderService.findByState(state);
        return ResponseEntity.ok(response);
    }

    //FILTRAR POR CANAL DE VENTA DEL PEDIDO
    @GetMapping("/sale-channel/{saleChannel}")
    public ResponseEntity<List<OrderDTOResponse>> findBySaleChannel (
            @PathVariable SaleChannelEnum saleChannel)
    {
        List<OrderDTOResponse> response = orderService.findBySaleChannel(saleChannel);
        return ResponseEntity.ok(response);
    }

    //HISTORIAL ENTRE FECHAS
    @GetMapping("/between-dates")
    public ResponseEntity<List<OrderDTOResponse>> findBetweenDates(
            @RequestParam LocalDate starDate,
            @RequestParam LocalDate endDate)
    {
        List<OrderDTOResponse> response = orderService.findBetweenDates(starDate, endDate);
        return ResponseEntity.ok(response);
    }

    //REPORTE DIARIO
    @GetMapping("/daily-report")
    public DailyReportDTO generateDailyReport() {
        return orderService.generateDailyReport();
    }

    //REPORTE MENSUAL
    @GetMapping("/report/monthly")
    public ResponseEntity<MonthlyReportDTO> generateMonthlyReport (
            @RequestParam int year, @RequestParam int month
    )
    {
        return ResponseEntity.ok(orderService.generateMonthlyReport(year, month));
    }
}
