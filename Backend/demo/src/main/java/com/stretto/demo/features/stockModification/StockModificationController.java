package com.stretto.demo.features.stockModification;

import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTORequest;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stockModification")
public class StockModificationController {
    private final StockModificationService stockModificationService;

    @PostMapping
    public ResponseEntity<StockModificationDTOResponse> create(@Valid @RequestBody StockModificationDTORequest request)
    {
        StockModificationDTOResponse response = stockModificationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StockModificationDTOResponse>> findAll()
    {
        List<StockModificationDTOResponse> responses = stockModificationService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockModificationDTOResponse> findById(@PathVariable Long id)
    {
        StockModificationDTOResponse response = stockModificationService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockModificationDTOResponse> update(@PathVariable Long id, @RequestBody StockModificationDTORequest request)
    {
        StockModificationDTOResponse response = stockModificationService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<StockModificationDTOResponse>> findByDate(@PathVariable LocalDate date)
    {
        List<StockModificationDTOResponse> responses = stockModificationService.findByDate(date);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/register")
    public ResponseEntity<StockModificationDTOResponse> register(@RequestBody StockModificationDTORequest request)
    {
        StockModificationDTOResponse response = stockModificationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(
            @RequestParam Long stockId,
            @RequestParam Double qty
    ){

        Boolean response = stockModificationService.validateStock(stockId, qty);
        return ResponseEntity.ok(response);
    }
}
