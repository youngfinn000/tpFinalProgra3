package com.stretto.demo.features.stockModification;

import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTORequest;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTOResponse;

import java.time.LocalDate;
import java.util.List;

public interface StockModificationService {

    StockModificationDTOResponse create(StockModificationDTORequest request);

    List<StockModificationDTOResponse> findAll();

    StockModificationDTOResponse findById(Long id);

    StockModificationDTOResponse update(Long id, StockModificationDTORequest request);

    void delete(Long id);

    List<StockModificationDTOResponse> findByDate(LocalDate date);
}
