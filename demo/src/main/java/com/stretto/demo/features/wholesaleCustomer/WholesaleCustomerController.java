package com.stretto.demo.features.wholesaleCustomer;


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
@RequestMapping("/api/wholesale-customers")
@RequiredArgsConstructor
public class WholesaleCustomerController {

    private final WholesaleCustomerService wholesaleCustomerService;

    //Lo crea
    @PostMapping
    public ResponseEntity<WholesaleCusDtoResponse> createWholesaleCustomer(
            @Valid @RequestBody WholesaleCusDtoRequest request
    ){
        WholesaleCusDtoResponse response = wholesaleCustomerService.createWholesaleCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Get all
    @GetMapping
    public ResponseEntity<List<WholesaleCusDtoResponse>> getAllWholesaleCustomer(){

        List<WholesaleCusDtoResponse> customers = wholesaleCustomerService.getAllWholesaleCustomer();
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    //Get by id
    @GetMapping("/{id}")
    public ResponseEntity<WholesaleCusDtoResponse> getWholesaleCustomerById(@PathVariable Long id){

        WholesaleCusDtoResponse customer = wholesaleCustomerService.getWholesaleCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    //Update by id
    @PutMapping("/{id}")
    public ResponseEntity<WholesaleCusDtoResponse> updateWholesaleCustomer( @PathVariable Long id, @Valid @RequestBody WholesaleCusDtoRequest request){

        WholesaleCusDtoResponse updateCustomer = wholesaleCustomerService.updateWholesaleCustomer(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(updateCustomer);
    }


    //SOFT delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteWholesaleCustomerById(@PathVariable Long id){

        wholesaleCustomerService.deleteWholesaleCustomer(id);
        return ResponseEntity.noContent().build();
    }

    //Search by EMAIL
    @GetMapping("/search")
    public ResponseEntity <WholesaleCusDtoResponse> getWholesaleCustomerByEmail(@RequestParam String email){

        WholesaleCusDtoResponse customer= wholesaleCustomerService.getWholesaleCustomerByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    //Get orders dependiendo un id de mayorista
    @GetMapping("/{id}/orders")
    public ResponseEntity<?>  getWholesaleCustomerOrders(@PathVariable Long id){
        return ResponseEntity.ok(wholesaleCustomerService.getWholesaleCustomerById(id));
    }

}
