package com.stretto.demo.features.wholesaleCustomer;

import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoRequest;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoResponse;
import com.stretto.demo.features.wholesaleCustomer.domain.mapper.WholesaleCusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WholesaleCustomerServiceImpl implements WholesaleCustomerService {

    private final WholesaleCustomerRepository wholesaleCustomerRepository;


    @Override
    public WholesaleCusDtoResponse createWholesaleCustomer(
            WholesaleCusDtoRequest request
    ) {
        WholesaleCustomerEntity entity = WholesaleCusMapper.toEntity(request);

        return WholesaleCusMapper.toResponse(wholesaleCustomerRepository.save(entity));
    }

    @Override
    public List<WholesaleCusDtoResponse> getAllWholesaleCustomer() {

        List<WholesaleCustomerEntity> customers =
                wholesaleCustomerRepository.findAll();

        return customers.stream()
                .map(WholesaleCusMapper::toResponse)
                .toList();
    }

    @Override
    public WholesaleCusDtoResponse getWholesaleCustomerById(Long id) {

        WholesaleCustomerEntity customer = wholesaleCustomerRepository.findById(id).orElseThrow(()->new RuntimeException("WholesaleCustomer not found"));
        return WholesaleCusMapper.toResponse(customer);
    }

    @Override
    public WholesaleCusDtoResponse updateWholesaleCustomer(Long id, WholesaleCusDtoRequest request) {
        WholesaleCustomerEntity customer = wholesaleCustomerRepository.findById(id).orElseThrow(()->new RuntimeException("WholesaleCustomer not found"));
        customer.setCompanyName(request.getCompanyName());
        customer.setEmail(request.getEmail());
        customer.setContactName(request.getContactName());
        customer.setCuit(request.getCuit());

        return WholesaleCusMapper.toResponse(wholesaleCustomerRepository.save(customer));
    }

    @Override
    public void deleteWholesaleCustomer(Long id) {
        WholesaleCustomerEntity customer = wholesaleCustomerRepository.findById(id).orElseThrow(()->new RuntimeException("WholesaleCustomer not found"));
        customer.setActive(false);
        wholesaleCustomerRepository.save(customer);
    }

    @Override
    public WholesaleCusDtoResponse getWholesaleCustomerByEmail(String email) {
        WholesaleCustomerEntity customer = wholesaleCustomerRepository.findByEmail(email).orElseThrow(()->new RuntimeException("WholesaleCustomer not found"));
        return WholesaleCusMapper.toResponse(customer);
    }



}
