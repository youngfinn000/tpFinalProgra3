package com.stretto.demo.features.ingredient;

import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.ingredient.domain.dto.IngredientDTORequest;
import com.stretto.demo.features.ingredient.domain.dto.IngredientDTOResponse;
import com.stretto.demo.features.ingredient.domain.mapper.IngredientMapper;
import com.stretto.demo.features.recipe.RecipeRepository;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.stock.StockEntity;
import com.stretto.demo.features.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repository;
    private final StockRepository stockRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public IngredientDTOResponse create(IngredientDTORequest request) {
        StockEntity stockId = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock Not Found"));

        RecipeEntity recipeId = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe Not Found"));

        IngredientEntity entity = IngredientMapper.toEntity(request, stockId, recipeId);
        IngredientEntity saved = repository.save(entity);

        return IngredientMapper.toResponse(saved);
    }

    @Override
    public List<IngredientDTOResponse> findAll() {
        List<IngredientEntity> entities = repository.findAll();
        return entities.stream()
                .map(IngredientMapper::toResponse)
                .toList();
    }

    @Override
    public IngredientDTOResponse findById(Long id) {
        IngredientEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient Not Found"));
    return IngredientMapper.toResponse(entity);
    }

    @Override
    public IngredientDTOResponse update(IngredientDTORequest request, Long id) {
        IngredientEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient Not Found"));

        entity.setRequiredAmount(request.getRequiredAmount());
        entity.setUnit(request.getUnit());

        IngredientEntity saved = repository.save(entity);
        return IngredientMapper.toResponse(saved);
    }
}
