package com.stretto.demo.features.stockModification;

import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.stock.domain.dto.StockDTORequest;
import com.stretto.demo.features.stock.domain.dto.StockDTOResponse;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTORequest;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTOResponse;

import java.time.LocalDate;
import java.util.List;

public interface StockModificationService {

    StockModificationDTOResponse create(StockModificationDTORequest request);

    List<StockModificationDTOResponse> findAll();

    StockModificationDTOResponse findById(Long id);

    StockModificationDTOResponse update(Long id, StockModificationDTORequest request);

    List<StockModificationDTOResponse> findByDate(LocalDate date);

    StockModificationDTOResponse register(StockModificationDTORequest request);

    Boolean validateStock(Long stockId, Double qty);

}
