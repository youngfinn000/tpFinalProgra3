package com.stretto.demo.features.wholesaleCustomer;

import com.stretto.demo.common.exception.AlreadyExistsException;
import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoRequest;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoResponse;
import com.stretto.demo.features.wholesaleCustomer.domain.mapper.WholesaleCusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WholesaleCustomerServiceImpl implements WholesaleCustomerService {

    private final WholesaleCustomerRepository wholesaleCustomerRepository;


    @Override
    public WholesaleCusDtoResponse createWholesaleCustomer(WholesaleCusDtoRequest request) {
        wholesaleCustomerRepository.findByEmail(request.getEmail()).orElseThrow(()-> new AlreadyExistsException("There is already a customer with an email: " + request.getEmail()));
        WholesaleCustomerEntity entity = WholesaleCusMapper.toEntity(request);
        return WholesaleCusMapper.toResponse(wholesaleCustomerRepository.save(entity));
    }

    @Override
    public WholesaleCusDtoResponse updateWholesaleCustomer(Long id, WholesaleCusDtoRequest request) {
        WholesaleCustomerEntity customer= findActiveOrThrow(id);
        if(wholesaleCustomerRepository.existsByEmailAndIdNot(request.getCuit(), id)){
            throw new AlreadyExistsException("There is already a customer with an email: " + request.getEmail());
        }
        if(request.getCuit() != null && wholesaleCustomerRepository.existsByCuitAndIdNot(request.getCuit(), id)){
            throw new AlreadyExistsException("There cuit is already registered for another client: "+request.getCuit());
        }
        customer.setCompanyName(request.getCompanyName());
        customer.setEmail(request.getEmail());
        customer.setContactName(request.getContactName());
        customer.setCuit(request.getCuit());
        return WholesaleCusMapper.toResponse(wholesaleCustomerRepository.save(customer));
    }

    @Override
    public void deleteWholesaleCustomer(Long id) {
        WholesaleCustomerEntity customer = findActiveOrThrow(id);
        customer.setActive(false);
        wholesaleCustomerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleCusDtoResponse getWholesaleCustomerById(Long id) {
        WholesaleCustomerEntity customer= wholesaleCustomerRepository.findById(id).orElseThrow(()->new NotFoundException("WholesaleCustomer not found with id: "+id));
        return WholesaleCusMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleCusDtoResponse> getAllWholesaleCustomer(){
        return wholesaleCustomerRepository.findByActiveTrue()
                .stream()
                .map(WholesaleCusMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleCusDtoResponse getWholesaleCustomerByEmail(String email) {
        WholesaleCustomerEntity customer = wholesaleCustomerRepository.findByEmail(email).orElseThrow(()->new NotFoundException("WholesaleCustomer not found with email: "+email));
        return WholesaleCusMapper.toResponse(customer);
    }

    private WholesaleCustomerEntity findActiveOrThrow(Long id) {
        WholesaleCustomerEntity customer= wholesaleCustomerRepository.findById(id).orElseThrow(()->new NotFoundException("WholesaleCustomer not found with id: "+id));
        if(customer.isActive()){
            throw new InvalidStateException("Wholesale Customer with id "+id+ " its inactive. " );
        }
        return customer;
    }
}
