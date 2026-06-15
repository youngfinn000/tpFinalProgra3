package com.stretto.demo.features.stock;

import com.stretto.demo.features.stock.domain.dto.StockDTORequest;
import com.stretto.demo.features.stock.domain.dto.StockDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockController {
    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockDTOResponse> create(@Valid @RequestBody StockDTORequest request)
    {
        StockDTOResponse response = stockService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StockDTOResponse>> findAll()
    {
        List<StockDTOResponse> response = stockService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTOResponse> findById(@PathVariable Long id)
    {
        StockDTOResponse response = stockService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDTOResponse> update(@PathVariable Long id, @RequestBody StockDTORequest request)
    {
        StockDTOResponse response = stockService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ToDiscount")
    public ResponseEntity<StockDTOResponse> ToDiscount(@PathVariable Long id, @RequestParam Double qty)
    {
        StockDTOResponse response = stockService.ToDiscount(id, qty);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/is-low")
    public ResponseEntity<Boolean> isLow(@PathVariable Long id){
        Boolean response = stockService.isLow(id);
        return ResponseEntity.ok(response);
    }


}
