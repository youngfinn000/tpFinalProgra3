package com.stretto.demo.features.flavors;

import com.stretto.demo.features.flavors.domain.dto.FlavorsDTORequest;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flavors")
public class FlavorsController {

    private final FlavorsService flavorsService;

    //crear
   @PostMapping
    public ResponseEntity<FlavorsDTOResponse> create (@Valid @RequestBody FlavorsDTORequest request)
   {
       FlavorsDTOResponse response = flavorsService.create(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   //listar todos
    @GetMapping
    public ResponseEntity<List<FlavorsDTOResponse>> findAll()
    {
       List<FlavorsDTOResponse> response = flavorsService.findAll();
       return ResponseEntity.ok(response);
    }

    //buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<FlavorsDTOResponse> findById (@PathVariable Long id)
    {
        FlavorsDTOResponse response = flavorsService.findById(id);
        return ResponseEntity.ok(response);
    }

    //actualizar
    @PutMapping("/{id}")
    public ResponseEntity<FlavorsDTOResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody FlavorsDTORequest request)
    {
        FlavorsDTOResponse response = flavorsService.update(id, request);
        return ResponseEntity.ok(response);
    }

    //eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete (@PathVariable Long id)
    {
        flavorsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //reactivar flavor
    @PatchMapping("/{id}/activate")
    public ResponseEntity<FlavorsDTOResponse> activate (@PathVariable Long id)
    {
        FlavorsDTOResponse response = flavorsService.activate(id);
        return ResponseEntity.ok(response);
    }

}
