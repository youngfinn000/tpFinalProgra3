package com.stretto.demo.features.stockModification;

import com.stretto.demo.common.exception.InsufficientStockException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.stock.domain.StockEntity;
import com.stretto.demo.features.stock.StockRepository;
import com.stretto.demo.features.stockModification.domain.StockModificationEntity;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTORequest;
import com.stretto.demo.features.stockModification.domain.dto.StockModificationDTOResponse;
import com.stretto.demo.features.stockModification.domain.enums.AdjustmentTypeEnum;
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
                .orElseThrow(() -> new NotFoundException("Stock Id not found"));

        InternalUserEntity userId = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User Id not found"));

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
                .orElseThrow(() -> new NotFoundException("Stock Id not found"));

        return StockModificationMapper.toResponse(entity);
    }


    @Override
    public StockModificationDTOResponse update(Long id, StockModificationDTORequest request) {
        StockModificationEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock Id not found"));

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
    @Override
    public StockModificationDTOResponse register(StockModificationDTORequest request) {
        StockEntity stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new NotFoundException("Stock Not Found"));

        InternalUserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(request.getAdjustmentType() == AdjustmentTypeEnum.ADD){
            stock.setCurrentStock(stock.getCurrentStock() + request.getAmount());
        }

        if(request.getAdjustmentType() == AdjustmentTypeEnum.REMOVE){
            Boolean validateStock = validateStock(stock.getId(), request.getAmount());

            if(validateStock){
                stock.setCurrentStock(stock.getCurrentStock() - request.getAmount());
            } else {
                throw new InsufficientStockException("Insufficiente stock");
            }
        }

        StockEntity saved = stockRepository.save(stock);
        StockModificationEntity entity = StockModificationMapper.toEntity(request, saved, user);
        StockModificationEntity savedModification = repository.save(entity);

        return StockModificationMapper.toResponse(savedModification);
    }

    @Override
    public Boolean validateStock(Long stockId, Double qty) {
        StockEntity entity = stockRepository.findById(stockId)
                .orElseThrow(() -> new NotFoundException("Stock Not Found"));

        return entity.getCurrentStock() >= qty;
    }
}
