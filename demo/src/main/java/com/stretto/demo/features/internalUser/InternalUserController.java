package com.stretto.demo.features.internalUser;

import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internalUser")
public class InternalUserController {
    private final InternalUserService internalUserService;

    @PostMapping
    public ResponseEntity<InternalUserDTOResponse> create(@Valid @RequestBody InternalUserDTORequest request)
    {
        InternalUserDTOResponse response = internalUserService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InternalUserDTOResponse>> findAll()
    {
        List<InternalUserDTOResponse> responses = internalUserService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternalUserDTOResponse> findById(@PathVariable Long id)
    {
        InternalUserDTOResponse response = internalUserService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternalUserDTOResponse> update(@PathVariable Long id,
                                                           @RequestBody InternalUserDTORequest request){
        InternalUserDTOResponse response = internalUserService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        internalUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<InternalUserDTOResponse> activate(@PathVariable Long id)
    {
        InternalUserDTOResponse response = internalUserService.activate(id);
        return ResponseEntity.ok(response);
    }
}
