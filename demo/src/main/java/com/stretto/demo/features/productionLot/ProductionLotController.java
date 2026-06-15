package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotActivityDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productionLot")
public class ProductionLotController {
    private final ProductionLotService productionLotService;

    @PostMapping
    public ResponseEntity<ProductionLotDTOResponse> create(@Valid @RequestBody ProductionLotDTORequest request)
    {
        ProductionLotDTOResponse response = productionLotService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductionLotDTOResponse>> findAll()
    {
        List<ProductionLotDTOResponse> response = productionLotService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionLotDTOResponse> findById(@PathVariable Long id)
    {
        ProductionLotDTOResponse response = productionLotService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionLotDTOResponse> update(@PathVariable Long id, @RequestBody ProductionLotDTORequest request)
    {
        ProductionLotDTOResponse response = productionLotService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ProductionLotDTOResponse> confirm(@PathVariable Long id)
    {
        ProductionLotDTOResponse response = productionLotService.confirm(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ProductionLotDTOResponse> cancel(@PathVariable Long id)
    {
        ProductionLotDTOResponse response = productionLotService.cancel(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/calculate-performance")
    public ResponseEntity<ProductionLotDTOResponse> calcularPerformance(@PathVariable Long id)
    {
        ProductionLotDTOResponse response = productionLotService.calculatePerformance(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ProductionLotActivityDTOResponse>> getProductionHistory(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long flavorId
    ) {
        List<ProductionLotActivityDTOResponse> response = productionLotService.getProductionHistory(date, flavorId);
        return ResponseEntity.ok(response);
    }
}
