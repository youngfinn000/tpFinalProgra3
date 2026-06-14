package com.stretto.demo.features.requestBudget;

import com.stretto.demo.common.exception.WholeSaleCustomerNotFoundException;
import com.stretto.demo.features.flavors.FlavorsRepository;
import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetStateDto;
import com.stretto.demo.features.requestBudget.domain.enums.StateRequestEnum;
import com.stretto.demo.features.requestBudget.domain.mapper.RequestBudgetMapper;
import com.stretto.demo.features.wholesaleCustomer.WholesaleCustomerRepository;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestBudgetServiceImpl implements RequestBudgetService {

    private final RequestBudgetRepository requestBudgetRepository;
    private final WholesaleCustomerRepository wholesaleCustomerRepository;
    private final FlavorsRepository flavorsRepository;


    @Override
    public RequestBudgetDtoResponse createRequestBudget(RequestBudgetDtoRequest request) {
        WholesaleCustomerEntity customer= wholesaleCustomerRepository.findById(request.getCustomerId()).orElseThrow(()-> new WholeSaleCustomerNotFoundException("Customer not found with id: "+request.getCustomerId()));
        List<FlavorsEntity> flavors = flavorsRepository.findAllById(request.getFlavorsId());
        if (flavors.isEmpty()) {
            throw new IllegalArgumentException("You must select an available flavor");
        }
        RequestBudgetEntity entity = RequestBudgetMapper.toEntity(request, customer, flavors);
        return RequestBudgetMapper.toResponse(requestBudgetRepository.save(entity));
    }

    @Override
    public List<RequestBudgetDtoResponse> getRequestBudgetByCustomer(Long customerId){
        WholesaleCustomerEntity customer=wholesaleCustomerRepository.findById(customerId).orElseThrow(()-> new WholeSaleCustomerNotFoundException("Customer not found with id: "+customerId));

        return requestBudgetRepository.findByCustomerId(customerId)
                .stream()
                .map(RequestBudgetMapper::toResponse)
                .toList();
    }

    @Override
    public List<RequestBudgetDtoResponse> getAllRequest(String state){
        List<RequestBudgetEntity> results;
        if(!state.isBlank()){
            StateRequestEnum stateEnum= StateRequestEnum.valueOf(state.toUpperCase());
            results= requestBudgetRepository.findByStateRequestEnum(stateEnum);
        }else {
            results= requestBudgetRepository.findAll();
        }
        return results.stream()
                .map(RequestBudgetMapper::toResponse)
                .toList();
    }

    @Override
    public RequestBudgetDtoResponse getRequestById(Long id){
        RequestBudgetEntity entity=requestBudgetRepository.findById(id).orElseThrow(()->new RuntimeException("Request id not found with id: "+id));
        return RequestBudgetMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public RequestBudgetDtoResponse updateRequestBudget(Long id, RequestBudgetStateDto statedto){
        RequestBudgetEntity entity = requestBudgetRepository.findById(id).orElseThrow(()-> new RuntimeException("RequestBudgetEntity not found")) ;

        if(entity.getStateRequestEnum().equals(StateRequestEnum.PENDING)){
            throw new IllegalStateException("Request budget state is PENDING");
        }

        StateRequestEnum newState = StateRequestEnum.valueOf(statedto.getState().toUpperCase());

        if(newState.equals(StateRequestEnum.CONFIRMED)){
            if(statedto.getBudget() <=0) {
                throw new IllegalStateException("Request budget state is CONFIRMED");
            }
                entity.setBudget(statedto.getBudget());
        }
        entity.setStateRequestEnum(newState);
        RequestBudgetEntity updated=requestBudgetRepository.save(entity);
        return RequestBudgetMapper.toResponse(updated);
    }



}
