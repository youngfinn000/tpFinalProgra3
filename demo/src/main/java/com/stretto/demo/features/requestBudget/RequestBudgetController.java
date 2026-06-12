package com.stretto.demo.features.requestBudget;


import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
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
    public ResponseEntity<RequestBudgetDtoResponse> createRequestBudget(
            @Valid @RequestBody RequestBudgetDtoRequest request) {
        RequestBudgetDtoResponse response = requestBudgetService.createRequestBudget(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RequestBudgetDtoResponse>> getAllRequestBudget(){
        List<RequestBudgetDtoResponse> requests = requestBudgetService.getAllRequestBudget();
        return ResponseEntity.status(HttpStatus.OK).body(requests);
    }

    @GetMapping
    public ResponseEntity< RequestBudgetDtoResponse> getRequestBudgetById (@PathVariable Long id){
        RequestBudgetDtoResponse request = requestBudgetService.getRequestBudgetById(id);
        return ResponseEntity.status(HttpStatus.OK).body(request);
    }

    @PutMapping
    public ResponseEntity<RequestBudgetDtoResponse> updateRequestBudget(@PathVariable Long id, @Valid @RequestBody RequestBudgetDtoRequest request) {
        RequestBudgetDtoResponse response = requestBudgetService.updateRequestBudget(id, request);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }





}
