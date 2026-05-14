package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;

import java.util.List;

public interface ProductionLotService {
    ProductionLotDTOResponse create(ProductionLotDTORequest request);

    List<ProductionLotDTOResponse> findAll();

    ProductionLotDTOResponse findById(Long id);

    ProductionLotDTOResponse update(ProductionLotDTORequest request, Long id);
}
