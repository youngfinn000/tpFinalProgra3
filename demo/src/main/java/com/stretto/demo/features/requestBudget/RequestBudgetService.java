package com.stretto.demo.features.requestBudget;

import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;

import java.util.List;

public interface RequestBudgetService {

    RequestBudgetDtoResponse createRequestBudget(RequestBudgetDtoRequest request);
    List<RequestBudgetDtoResponse> getAllRequestBudget();
    RequestBudgetDtoResponse getRequestBudgetById(Long id);
    RequestBudgetDtoResponse updateRequestBudget(Long id,RequestBudgetDtoRequest request);





}
