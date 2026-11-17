package com.stretto.demo.features.recipe;

import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.flavors.FlavorsRepository;
import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.ingredient.IngredientRepository;
import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.product.ProductRepository;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.productionLot.ProductionLotRepository;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;
import com.stretto.demo.features.recipe.domain.dto.RecipeSuppliesDTOResponse;
import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import com.stretto.demo.features.recipe.domain.mapper.RecipeMapper;
import com.stretto.demo.features.stock.StockRepository;
import com.stretto.demo.features.stock.domain.StockEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeImpl implements RecipeService {

    private final RecipeRepository repository;
    private final ProductionLotRepository productionLotRepository;
    private final ProductRepository productRepository;
    private final FlavorsRepository flavorsRepository;
    private final IngredientRepository ingredientRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional
    public RecipeDTOResponse create(RecipeDTORequest request) {
        FlavorsEntity flavor = null;
        if (request.getFlavorId() != null) {
            flavor = flavorsRepository.findById(request.getFlavorId())
                    .orElseThrow(() -> new NotFoundException("Flavor not found"));
        }

        ProductEntity product = null;
        if (request.getProductId() != null) {
            product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
        }

        ProductionLotEntity productionLot = null;
        if (request.getProductionLotId() != null) {
            productionLot = productionLotRepository.findById(request.getProductionLotId())
                    .orElseThrow(() -> new NotFoundException("Production Lot not found"));
        }

        List<RecipeEntity> subRecipes = request.getSubRecipeIds() != null && !request.getSubRecipeIds().isEmpty()
                ? repository.findAllById(request.getSubRecipeIds())
                : new ArrayList<>();

        RecipeEntity entity = RecipeMapper.toEntity(
                request,
                flavor,
                product,
                productionLot,
                new ArrayList<>(),
                subRecipes
        );

        List<IngredientEntity> ingredients = buildIngredients(request, entity);
        entity.getIngredients().addAll(ingredients);

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
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        return RecipeMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public RecipeDTOResponse update(RecipeDTORequest request, Long id) {
        RecipeEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        if (request.getFlavorId() != null) {
            FlavorsEntity flavor = flavorsRepository.findById(request.getFlavorId())
                    .orElseThrow(() -> new NotFoundException("Flavor not found"));
            entity.setFlavor(flavor);
        }

        if (request.getProductId() != null) {
            ProductEntity product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            entity.setProduct(product);
        }

        String resolvedName = (request.getName() != null && !request.getName().isBlank())
                ? request.getName().trim()
                : (entity.getFlavor() != null ? entity.getFlavor().getName() : entity.getName());

        entity.setName(resolvedName);
        entity.setBaseQuantity(request.getBaseQuantity());
        entity.setUnitBase(request.getUnitBase() != null ? request.getUnitBase() : UnitMeasurementEnum.KG);

        if (request.getProductionLotId() != null) {
            ProductionLotEntity productionLot = productionLotRepository.findById(request.getProductionLotId())
                    .orElseThrow(() -> new NotFoundException("Production Lot not found"));
            entity.setProductionLot(productionLot);
        }

        if (request.getIngredients() != null || request.getIngredientIds() != null) {
            if (entity.getIngredients() == null) {
                entity.setIngredients(new ArrayList<>());
            } else {
                entity.getIngredients().clear();
            }
            List<IngredientEntity> updatedIngredients = buildIngredients(request, entity);
            entity.getIngredients().addAll(updatedIngredients);
        }

        RecipeEntity saved = repository.save(entity);
        return RecipeMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RecipeEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        List<ProductionLotEntity> lots = productionLotRepository.findByRecipe_Id(id);
        if (!lots.isEmpty()) {
            productionLotRepository.deleteAll(lots);
        }

        repository.delete(entity);
    }

    @Override
    public List<RecipeSuppliesDTOResponse> calculateIngredients(Long recipeId, Double kg) {
        RecipeEntity entity = repository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        Double baseQty = (entity.getBaseQuantity() != null && entity.getBaseQuantity() > 0)
                ? entity.getBaseQuantity()
                : 1.0;

        List<IngredientEntity> ingredients = entity.getIngredients() != null
                ? entity.getIngredients()
                : Collections.emptyList();

        return ingredients.stream()
                .map(i -> {
                    UnitMeasurementEnum unit = i.getUnit() != null
                            ? i.getUnit()
                            : (i.getStock() != null ? i.getStock().getUnit() : null);
                    return RecipeSuppliesDTOResponse.builder()
                            .id(i.getId())
                            .stockId(i.getStock() != null ? i.getStock().getId() : null)
                            .ingredientName(i.getStock() != null ? i.getStock().getName() : "")
                            .baseQuantity(i.getRequiredAmount())
                            .requiredQuantity((kg / baseQty) * i.getRequiredAmount())
                            .unit(unit)
                            .build();
                })
                .toList();
    }

    private List<IngredientEntity> buildIngredients(RecipeDTORequest request, RecipeEntity recipe) {
        List<IngredientEntity> result = new ArrayList<>();

        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            for (RecipeDTORequest.RecipeIngredientRequest item : request.getIngredients()) {
                StockEntity stock = stockRepository.findById(item.getStockId())
                        .orElseThrow(() -> new NotFoundException("Stock not found with id: " + item.getStockId()));

                UnitMeasurementEnum unit = item.getUnit() != null ? item.getUnit() : stock.getUnit();

                IngredientEntity ing = IngredientEntity.builder()
                        .recipe(recipe)
                        .stock(stock)
                        .requiredAmount(item.getRequiredAmount())
                        .unit(unit)
                        .build();
                result.add(ing);
            }
        } else if (request.getIngredientIds() != null && !request.getIngredientIds().isEmpty()) {
            List<IngredientEntity> existing = ingredientRepository.findAllById(request.getIngredientIds());
            for (IngredientEntity ing : existing) {
                ing.setRecipe(recipe);
                result.add(ing);
            }
        }

        return result;
    }
}
