package com.stretto.demo.features.stockModification;

import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.stock.domain.StockEntity;
import com.stretto.demo.features.stock.StockRepository;
import com.stretto.demo.features.stockModification.domain.StockModificationEntity;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTORequest;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTOResponse;
import com.stretto.demo.features.stockModification.domain.mapper.StockModificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockModificationImpl implements StockModificationService{

    private final StockModificationRepository repository;
    private final StockRepository stockRepository;
    private final InternalUserRepository userRepository;

    @Override
    public StockModificationDTOResponse create(StockModificationDTORequest request) {
        StockEntity stockId = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock Id not found"));

        InternalUserEntity userId = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User Id not found"));

        StockModificationEntity entity = StockModificationMapper.toEntity(request, stockId, userId);
        StockModificationEntity saved = repository.save(entity);
        return StockModificationMapper.toResponse(saved);
    }

    @Override
    public List<StockModificationDTOResponse> findAll() {
        List<StockModificationEntity> entities = repository.findAll();
        return entities.stream()
                .map(StockModificationMapper::toResponse)
                .toList();
    }

    @Override
    public StockModificationDTOResponse findById(Long id) {
        StockModificationEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Id not found"));

        return StockModificationMapper.toResponse(entity);
    }

    @Override
    public StockModificationDTOResponse update(Long id, StockModificationDTORequest request) {
        StockModificationEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Id not found"));

        entity.setAdjustmentType(request.getAdjustmentType());
        entity.setAmount(request.getAmount());
        entity.setMotive(request.getMotive());
        StockModificationEntity updated = repository.save(entity);
        return StockModificationMapper.toResponse(updated);
    }

    @Override
    public List<StockModificationDTOResponse> findByDate(LocalDate date) {
        List<StockModificationEntity> entities = repository.findByModificationDate(date);
        return entities.stream()
                .map(StockModificationMapper::toResponse)
                .toList();
    }
}
