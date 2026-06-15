package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotActivityDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import com.stretto.demo.features.productionLot.domain.mapper.ProductionLotMapper;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionLotImpl implements ProductionLotService {

    private final ProductionLotRepository repository;
    private final RecipeRepository recipeRepository;
    private final InternalUserRepository internalUserRepository;

    @Override
    public ProductionLotDTOResponse create(ProductionLotDTORequest request) {
        RecipeEntity recipeId = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        InternalUserEntity internalUserId = internalUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Internal user not found"));

        ProductionLotEntity entity = ProductionLotMapper.toEntity(request, internalUserId, recipeId);
        ProductionLotEntity saved = repository.save(entity);
        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public List<ProductionLotDTOResponse> findAll() {
        List<ProductionLotEntity> entities = repository.findAll();
        return entities.stream()
                .map(ProductionLotMapper::toResponse)
                .toList();
    }

    @Override
    public ProductionLotDTOResponse findById(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production lot not found"));

        return ProductionLotMapper.toResponse(entity);
    }

    @Override
    public ProductionLotDTOResponse update(ProductionLotDTORequest request, Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production lot not found"));

        entity.setAmountProduced(request.getAmountProduced());
        entity.setStatus(request.getStatus());
        entity.setPerformancePct(request.getPerformancePCT());
        ProductionLotEntity saved = repository.save(entity);
        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public ProductionLotDTOResponse confirm(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production lot not found"));

        if(entity.getStatus() != StatusLotEnum.IN_PROGRESS){
            throw new RuntimeException("Cannot confirm a lot that is not IN_PROGRESS");
        }

        entity.setStatus(StatusLotEnum.COMPLETED);
        ProductionLotEntity saved = repository.save(entity);

        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public ProductionLotDTOResponse cancel(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production lot not found"));

        if(entity.getStatus() == StatusLotEnum.COMPLETED){
            throw new RuntimeException("Cannot canceled a lot that is COMPLETED");
        }

        if(entity.getStatus() == StatusLotEnum.CANCELLED){
            throw new RuntimeException("Cannot canceled a lot that is CANCELED");
        }

        entity.setStatus(StatusLotEnum.CANCELLED);
        ProductionLotEntity saved = repository.save(entity);

        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public ProductionLotDTOResponse calculatePerformance(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production lot not found"));

        entity.setPerformancePct((entity.getAmountProduced() / entity.getRecipe().getBaseQuantity()) * 100);
        ProductionLotEntity saved = repository.save(entity);
        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public List<ProductionLotActivityDTOResponse> getProductionHistory(LocalDate date, Long flavorId) {

        if(date != null && flavorId != null){
            return repository.findByProductionDateAndFlavorId(date, flavorId).stream()
                    .map(ProductionLotMapper::toActivityResponse)
                    .toList();
        }

        if(date != null){
            return repository.findByProductionDate(date).stream()
                    .map(ProductionLotMapper::toActivityResponse)
                    .toList();
        }

        if(flavorId != null){
            return repository.findByFlavorId(flavorId).stream()
                    .map(ProductionLotMapper::toActivityResponse)
                    .toList();
        }

        return repository.findAll().stream()
                .map(ProductionLotMapper::toActivityResponse)
                .toList();
    }
}
