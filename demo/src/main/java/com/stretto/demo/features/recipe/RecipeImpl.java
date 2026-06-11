package com.stretto.demo.features.recipe;

import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.ingredient.IngredientRepository;
import com.stretto.demo.features.product.ProductRepository;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.productionLot.ProductionLotRepository;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;
import com.stretto.demo.features.recipe.domain.dto.RecipeSuppliesDTOResponse;
import com.stretto.demo.features.recipe.domain.mapper.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeImpl implements RecipeService{

    private final RecipeRepository repository;
    private final ProductionLotRepository productionLotRepository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    @Override
    public RecipeDTOResponse create(RecipeDTORequest request) {
        ProductEntity productId = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductionLotEntity productionLotId = productionLotRepository.findById(request.getProductionLotId())
                .orElseThrow(() -> new RuntimeException("Prodctuion Lot not found"));

        List<IngredientEntity> ingredients = ingredientRepository.findAllById(request.getIngredientIds());

        List<RecipeEntity> subRecipes = repository.findAllById(request.getSubRecipeIds());

        RecipeEntity entity = RecipeMapper.toEntity(request, productId, productionLotId, ingredients, subRecipes);
        RecipeEntity saved = repository.save(entity);
        return RecipeMapper.toResponse(saved);
    }

    @Override
    public List<RecipeDTOResponse> findAll() {
        List<RecipeEntity> entities = repository.findAll();
        return entities.stream()
                .map(RecipeMapper::toResponse)
                .toList();
    }

    @Override
    public RecipeDTOResponse findById(Long id) {
        RecipeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return RecipeMapper.toResponse(entity);
    }

    @Override
    public RecipeDTOResponse update(RecipeDTORequest request, Long id) {
        RecipeEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        entity.setName(request.getName());
        entity.setBaseQuantity(request.getBaseQuantity());
        entity.setUnitBase(request.getUnitBase());

        RecipeEntity saved = repository.save(entity);
        return RecipeMapper.toResponse(saved);
    }

    @Override
    public List<RecipeSuppliesDTOResponse> calculateIngredients(Long recipeId, Double kg) {

        RecipeEntity entity = repository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        List<RecipeSuppliesDTOResponse> amount = entity.getIngredients()
                .stream()
                .map((i) -> RecipeSuppliesDTOResponse.builder()
                        .id(i.getId())
                        .ingredientName(i.getStock().getName())
                        .requiredQuantity((kg / entity.getBaseQuantity()) * i.getRequiredAmount())
                        .build()
                )
                .toList();

        return amount;
    }

}
