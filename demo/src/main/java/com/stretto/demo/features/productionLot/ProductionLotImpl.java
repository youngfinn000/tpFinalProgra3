package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.productionLot.domain.mapper.ProductionLotMapper;
import com.stretto.demo.features.recipe.RecipeEntity;
import com.stretto.demo.features.recipe.domain.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
