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

    //CREAR SABOR
   @PostMapping
    public ResponseEntity<FlavorsDTOResponse> create (@Valid @RequestBody FlavorsDTORequest request)
   {
       FlavorsDTOResponse response = flavorsService.create(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   //LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<FlavorsDTOResponse>> findAll()
    {
       List<FlavorsDTOResponse> response = flavorsService.findAll();
       return ResponseEntity.ok(response);
    }

    //BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<FlavorsDTOResponse> findById (@PathVariable Long id)
    {
        FlavorsDTOResponse response = flavorsService.findById(id);
        return ResponseEntity.ok(response);
    }

    //ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<FlavorsDTOResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody FlavorsDTORequest request)
    {
        FlavorsDTOResponse response = flavorsService.update(id, request);
        return ResponseEntity.ok(response);
    }

    //ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete (@PathVariable Long id)
    {
        flavorsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //REACTIVAR SABOR
    @PatchMapping("/{id}/activate")
    public ResponseEntity<FlavorsDTOResponse> activate (@PathVariable Long id)
    {
        FlavorsDTOResponse response = flavorsService.activate(id);
        return ResponseEntity.ok(response);
    }

    //BUSCAR POR NOMBRE
    @GetMapping("/name/{name}")
    public ResponseEntity<FlavorsDTOResponse> findByName (@PathVariable String name)
    {
        FlavorsDTOResponse response = flavorsService.findByName(name);
        return ResponseEntity.ok(response);
    }

    //BUSCAR INACTIVOS
    @GetMapping("/inactive")
    public ResponseEntity<List<FlavorsDTOResponse>> findInactive ()
    {
        List<FlavorsDTOResponse> response = flavorsService.findInactive();
        return ResponseEntity.ok(response);
    }

    //BUSCAR POR TEXTO
    @GetMapping("/search")
    public ResponseEntity<List<FlavorsDTOResponse>> searchByName (@RequestParam String name)
    {
        List<FlavorsDTOResponse> response = flavorsService.searchByName(name);
        return ResponseEntity.ok(response);
    }

}
