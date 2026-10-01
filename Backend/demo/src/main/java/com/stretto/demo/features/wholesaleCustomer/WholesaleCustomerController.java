package com.stretto.demo.features.wholesaleCustomer;


import com.stretto.demo.features.requestBudget.RequestBudgetService;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoRequest;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wholesale_customers")
@RequiredArgsConstructor
public class WholesaleCustomerController {

    private final WholesaleCustomerService wholesaleCustomerService;
    private final RequestBudgetService requestBudgetService;


    @PostMapping
    public ResponseEntity<WholesaleCusDtoResponse> createWholesaleCustomer(@Valid @RequestBody WholesaleCusDtoRequest request){
        WholesaleCusDtoResponse response = wholesaleCustomerService.createWholesaleCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WholesaleCusDtoResponse>> getAllWholesaleCustomer(){
        List<WholesaleCusDtoResponse> customers = wholesaleCustomerService.getAllWholesaleCustomer();
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }


    @GetMapping("/{id}")
    public ResponseEntity<WholesaleCusDtoResponse> getWholesaleCustomerById(@PathVariable Long id){
        WholesaleCusDtoResponse customer = wholesaleCustomerService.getWholesaleCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }


    @PutMapping("/{id}")
    public ResponseEntity<WholesaleCusDtoResponse> updateWholesaleCustomer( @PathVariable Long id, @Valid @RequestBody WholesaleCusDtoRequest request){
        WholesaleCusDtoResponse updateCustomer = wholesaleCustomerService.updateWholesaleCustomer(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(updateCustomer);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteWholesaleCustomerById(@PathVariable Long id){
        wholesaleCustomerService.deleteWholesaleCustomer(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity <WholesaleCusDtoResponse> getWholesaleCustomerByEmail(@RequestParam String email){
        WholesaleCusDtoResponse customer= wholesaleCustomerService.getWholesaleCustomerByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<RequestBudgetDtoResponse>>  getMyrequest(@PathVariable Long id, @RequestParam(required = false) String state){
        List<RequestBudgetDtoResponse> requests = (state != null && !state.isBlank())
                ? requestBudgetService.getAllRequest(state).stream()
                .filter(r -> r.getCustomerId().equals(id))
                .toList()
                : requestBudgetService.getRequestBudgetByCustomer(id);
        return ResponseEntity.ok(requests);
    }

}
