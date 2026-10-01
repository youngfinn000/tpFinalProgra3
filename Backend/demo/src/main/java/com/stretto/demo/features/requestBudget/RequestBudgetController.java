package com.stretto.demo.features.requestBudget;


import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetStateDto;
import com.stretto.demo.features.wholesaleCustomer.WholesaleCustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/request_budget")
@RequiredArgsConstructor
public class RequestBudgetController {

    private final WholesaleCustomerService wholesaleCustomerService;
    private final RequestBudgetService requestBudgetService;

    @PostMapping
    public ResponseEntity<RequestBudgetDtoResponse> createRequestBudget(@Valid @RequestBody RequestBudgetDtoRequest request) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(requestBudgetService.createRequestBudget(request));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RequestBudgetDtoResponse>> getRequestBudgetByCustomer(@Valid @PathVariable Long customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(requestBudgetService.getRequestBudgetByCustomer(customerId));
    }

    @GetMapping
    public ResponseEntity<List<RequestBudgetDtoResponse>> getAllRequest(@RequestParam(required = false) String state){
        return ResponseEntity.status(HttpStatus.OK).body(requestBudgetService.getAllRequest(state));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<RequestBudgetDtoResponse> updateState(@PathVariable Long id, @Valid @RequestBody RequestBudgetStateDto statedto) {
        return  ResponseEntity.status(HttpStatus.OK).body(requestBudgetService.updateRequestBudget(id, statedto));
    }





}
