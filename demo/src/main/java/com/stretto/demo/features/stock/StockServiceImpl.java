package com.stretto.demo.features.stock;

import com.stretto.demo.common.exception.InsufficientStockException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.stock.domain.StockEntity;
import com.stretto.demo.features.stock.domain.dto.StockDTORequest;
import com.stretto.demo.features.stock.domain.dto.StockDTOResponse;
import com.stretto.demo.features.stock.domain.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository repository;

    @Override
    public StockDTOResponse create(StockDTORequest request) {
        StockEntity response = StockMapper.toEntity(request);
        StockEntity saved = repository.save(response);

        return StockMapper.toResponse(saved);
    }

    @Override
    public List<StockDTOResponse> findAll() {
        List<StockEntity> entities = repository.findAll();
        return entities.stream()
                .map(StockMapper::toResponse)
                .toList();
    }

    @Override
    public StockDTOResponse findById(Long id) {
        StockEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock Not Found"));

        return StockMapper.toResponse(entity);
    }

    @Override
    public StockDTOResponse update(StockDTORequest request, Long id) {
        StockEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock Not Found"));

        entity.setName(request.getName());
        entity.setCurrentStock(request.getCurrentStock());
        entity.setMinimumStock(request.getMinimumStock());
        entity.setUnit(request.getUnitMeasurement());
        entity.setStatus(request.getStatus());

        StockEntity saved = repository.save(entity);
        return StockMapper.toResponse(saved);
    }



    @Override
    public StockDTOResponse ToDiscount(Long id, Double qty) {

        StockEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock Not Found"));

        if(entity.getCurrentStock() < qty){
            throw new InsufficientStockException("The quantity to be deducted is greater than the stock");
        }

        entity.setCurrentStock(entity.getCurrentStock() - qty);
        StockEntity saved = repository.save(entity);

        return StockMapper.toResponse(saved);
    }

    @Override
    public Boolean isLow(Long id) {

        StockEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock Not Found"));

        return entity.getCurrentStock() <= entity.getMinimumStock();
    }
}
