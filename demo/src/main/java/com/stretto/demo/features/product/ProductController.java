package com.stretto.demo.features.product;


import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    //CREAR
    @PostMapping
    public ResponseEntity<ProductDTOResponse> create(@Valid @RequestBody ProductDTORequest request) {
        ProductDTOResponse response = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //BUSCAR TODOS
    @GetMapping
    public ResponseEntity<List<ProductDTOResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    //BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    //ACTUALIZAR PRODUCTO
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody ProductDTORequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    //BAJA DE PRODUCTO POR ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //ACTIVAR PRODUCTO
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductDTOResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(productService.activate(id));
    }


    }

