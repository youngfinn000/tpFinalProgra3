package com.stretto.demo.features.recipe;

import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeDTOResponse> create(@Valid @RequestBody RecipeDTORequest request)
    {
        RecipeDTOResponse response = recipeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTOResponse>> findAll()
    {
        List<RecipeDTOResponse> responses = recipeService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTOResponse> findById(@PathVariable Long id)
    {
        RecipeDTOResponse response = recipeService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTOResponse> update(@PathVariable Long id, @RequestBody RecipeDTORequest request)
    {
        RecipeDTOResponse response = recipeService.update(request,id);
        return ResponseEntity.ok(response);
    }
}
