package com.stretto.demo.features.requestBudget;

import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoRequest;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetDtoResponse;
import com.stretto.demo.features.requestBudget.domain.dto.RequestBudgetStateDto;

import java.util.List;

public interface RequestBudgetService {

    // CLIENTE MAYORISTA: crear una solicitud de presupuesto
    RequestBudgetDtoResponse createRequestBudget(RequestBudgetDtoRequest request);

    // CLIENTE MAYORISTA: ver sus propias solicitudes
    List<RequestBudgetDtoResponse> getRequestBudgetByCustomer(Long customerId);

    // ADMIN: ver todas las solicitudes (opcionalmente filtradas por estado)
    List<RequestBudgetDtoResponse> getAllRequest(String state);

    // ADMIN: ver una solicitud específica
    RequestBudgetDtoResponse getRequestById(Long id);

    // ADMIN: aceptar o rechazar una solicitud
    RequestBudgetDtoResponse updateRequestBudget(Long id, RequestBudgetStateDto statedto);




}
