package com.stretto.demo.features.stock;

import com.stretto.demo.features.stock.domain.dto.StockDTORequest;
import com.stretto.demo.features.stock.domain.dto.StockDTOResponse;

import java.util.List;

public interface StockService {

    StockDTOResponse create(StockDTORequest request);

    List<StockDTOResponse> findAll();

    StockDTOResponse findById(Long id);

    StockDTOResponse update(StockDTORequest request, Long id);
}
