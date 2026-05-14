package com.stretto.demo.features.requestBudget;

import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
import com.stretto.demo.features.requestBudget.domain.mapper.RequestBudgetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestBudgetServiceImpl implements RequestBudgetService {

    private final RequestBudgetRepository requestBudgetRepository;

    @Override
    public RequestBudgetDtoResponse createRequestBudget(RequestBudgetDtoRequest request) {
        RequestBudgetEntity entity = RequestBudgetMapper.toEntity(request);
        return RequestBudgetMapper.toResponse(requestBudgetRepository.save(entity));
    }

    @Override
    public List<RequestBudgetDtoResponse> getAllRequestBudget(){
        List<RequestBudgetEntity> request = requestBudgetRepository.findAll();
        return request.stream().map(RequestBudgetMapper::toResponse).toList();
    }

    @Override
    public RequestBudgetDtoResponse getRequestBudgetById(Long id){
        RequestBudgetEntity entity = requestBudgetRepository.findById(id).orElseThrow(()-> new RuntimeException("RequestBudgetEntity not found")) ;
        return RequestBudgetMapper.toResponse(entity);
    }

    @Override
    public RequestBudgetDtoResponse updateRequestBudget(Long id, RequestBudgetDtoRequest request){
        RequestBudgetEntity entity = requestBudgetRepository.findById(id).orElseThrow(()-> new RuntimeException("RequestBudgetEntity not found")) ;
        entity.setQuantityKg(request.getQuantity());
        entity.setRequestDateTime(request.getRequestDatetime());
        entity.setAdvancePayment(request.isAdvancePayment());
        entity.setCustomer(request.getCustomer());
        entity.setListflavors(request.getFlavors());

        return RequestBudgetMapper.toResponse(requestBudgetRepository.save(entity));
    }



}
