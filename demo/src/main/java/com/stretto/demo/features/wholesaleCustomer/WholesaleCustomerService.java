package com.stretto.demo.features.wholesaleCustomer;

import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoRequest;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface WholesaleCustomerService {

    WholesaleCusDtoResponse createWholesaleCustomer(
            WholesaleCusDtoRequest request
    );

    List<WholesaleCusDtoResponse> getAllWholesaleCustomer();

    WholesaleCusDtoResponse getWholesaleCustomerById(Long id);

    WholesaleCusDtoResponse updateWholesaleCustomer(Long id, WholesaleCusDtoRequest request);

    void deleteWholesaleCustomer(Long id);

    WholesaleCusDtoResponse getWholesaleCustomerByEmail(String email);

}
