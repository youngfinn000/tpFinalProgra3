package com.stretto.demo.features.ingredient;

import com.stretto.demo.features.ingredient.domain.dto.IngredientDTORequest;
import com.stretto.demo.features.ingredient.domain.dto.IngredientDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<IngredientDTOResponse> create(@Valid @RequestBody IngredientDTORequest request)
    {
        IngredientDTOResponse response = ingredientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTOResponse>> findAll()
    {
        List<IngredientDTOResponse> response = ingredientService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTOResponse> findById(@PathVariable Long id)
    {
        IngredientDTOResponse response = ingredientService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTOResponse> update(@PathVariable Long id, @RequestBody IngredientDTORequest request)
    {
        IngredientDTOResponse response = ingredientService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/calculate-total")
    public ResponseEntity<Double> calculate(@PathVariable Long id, @RequestParam Double kg)
    {
        Double response = ingredientService.calculateTotalAmount(id, kg);
        return ResponseEntity.ok(response);
    }
}
