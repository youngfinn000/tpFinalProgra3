package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.productionLot.domain.dto.ProductionLotActivityDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;

import java.time.LocalDate;
import java.util.List;

public interface ProductionLotService {
    ProductionLotDTOResponse create(ProductionLotDTORequest request);

    List<ProductionLotDTOResponse> findAll();

    ProductionLotDTOResponse findById(Long id);

    ProductionLotDTOResponse update(ProductionLotDTORequest request, Long id);

    ProductionLotDTOResponse confirm(Long id);

    ProductionLotDTOResponse cancel(Long id);

    ProductionLotDTOResponse calculatePerformance(Long id);

    List<ProductionLotActivityDTOResponse> getProductionHistory(LocalDate date, Long flavorId);
}
