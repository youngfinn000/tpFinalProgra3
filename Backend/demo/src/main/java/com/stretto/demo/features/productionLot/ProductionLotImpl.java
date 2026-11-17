package com.stretto.demo.features.productionLot;

import com.stretto.demo.auth.credentials.CredentialsEntity;
import com.stretto.demo.auth.credentials.CredentialsRepository;
import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotActivityDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import com.stretto.demo.features.productionLot.domain.mapper.ProductionLotMapper;
import com.stretto.demo.features.recipe.RecipeRepository;
import com.stretto.demo.features.recipe.domain.RecipeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionLotImpl implements ProductionLotService {

    private final ProductionLotRepository repository;
    private final RecipeRepository recipeRepository;
    private final InternalUserRepository internalUserRepository;
    private final CredentialsRepository credentialsRepository;

    @Override
    public ProductionLotDTOResponse create(ProductionLotDTORequest request) {
        RecipeEntity recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        InternalUserEntity internalUser = resolveUser(request.getUserId());

        ProductionLotEntity entity = ProductionLotMapper.toEntity(request, internalUser, recipe);
        ProductionLotEntity saved = repository.save(entity);
        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public List<ProductionLotDTOResponse> findAll() {
        List<ProductionLotEntity> entities = repository.findAll();
        return entities.stream()
                .sorted(Comparator.comparing(
                        (ProductionLotEntity e) -> e.getProductionDate() != null ? e.getProductionDate() : LocalDate.MIN
                ).reversed().thenComparing(ProductionLotEntity::getId, Comparator.reverseOrder()))
                .map(ProductionLotMapper::toResponse)
                .toList();
    }

    @Override
    public ProductionLotDTOResponse findById(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Production lot not found"));

        return ProductionLotMapper.toResponse(entity);
    }

    @Override
    public ProductionLotDTOResponse update(ProductionLotDTORequest request, Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Production lot not found"));

        if (request.getRecipeId() != null) {
            RecipeEntity recipe = recipeRepository.findById(request.getRecipeId())
                    .orElseThrow(() -> new NotFoundException("Recipe not found"));
            entity.setRecipe(recipe);
        }

        if (request.getAmountProduced() != null) {
            entity.setAmountProduced(request.getAmountProduced());
        }
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }
        if (request.getProductionDate() != null) {
            entity.setProductionDate(request.getProductionDate());
        }
        if (request.getPerformancePCT() != null) {
            entity.setPerformancePct(request.getPerformancePCT());
        } else if (entity.getRecipe() != null
                && entity.getRecipe().getBaseQuantity() != null
                && entity.getRecipe().getBaseQuantity() > 0
                && entity.getAmountProduced() != null) {
            entity.setPerformancePct((entity.getAmountProduced() / entity.getRecipe().getBaseQuantity()) * 100.0);
        }

        ProductionLotEntity saved = repository.save(entity);
        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public ProductionLotDTOResponse confirm(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Production lot not found"));

        if (entity.getStatus() != StatusLotEnum.IN_PROGRESS) {
            throw new InvalidStateException("Cannot confirm a lot that is not IN_PROGRESS");
        }

        entity.setStatus(StatusLotEnum.COMPLETED);
        ProductionLotEntity saved = repository.save(entity);

        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public ProductionLotDTOResponse cancel(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Production lot not found"));

        if (entity.getStatus() == StatusLotEnum.COMPLETED) {
            throw new InvalidStateException("Cannot cancel a lot that is COMPLETED");
        }

        if (entity.getStatus() == StatusLotEnum.CANCELLED) {
            throw new InvalidStateException("Cannot cancel a lot that is CANCELLED");
        }

        entity.setStatus(StatusLotEnum.CANCELLED);
        ProductionLotEntity saved = repository.save(entity);

        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public ProductionLotDTOResponse calculatePerformance(Long id) {
        ProductionLotEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Production lot not found"));

        Double baseQuantity = entity.getRecipe().getBaseQuantity();
        if (baseQuantity == null || baseQuantity <= 0) {
            throw new InvalidStateException("Recipe base quantity must be greater than zero to calculate performance");
        }

        entity.setPerformancePct((entity.getAmountProduced() / baseQuantity) * 100);
        ProductionLotEntity saved = repository.save(entity);
        return ProductionLotMapper.toResponse(saved);
    }

    @Override
    public List<ProductionLotActivityDTOResponse> getProductionHistory(LocalDate date, Long recipeId, Long flavorId) {
        List<ProductionLotEntity> lots = (date != null)
                ? repository.findByProductionDate(date)
                : repository.findAll();

        return lots.stream()
                .filter(lot -> {
                    if (recipeId != null) {
                        return lot.getRecipe() != null && recipeId.equals(lot.getRecipe().getId());
                    }
                    return true;
                })
                .filter(lot -> {
                    if (flavorId != null) {
                        return lot.getRecipe() != null
                                && lot.getRecipe().getFlavor() != null
                                && flavorId.equals(lot.getRecipe().getFlavor().getId());
                    }
                    return true;
                })
                .sorted(Comparator.comparing(
                        (ProductionLotEntity e) -> e.getProductionDate() != null ? e.getProductionDate() : LocalDate.MIN
                ).reversed().thenComparing(ProductionLotEntity::getId, Comparator.reverseOrder()))
                .map(ProductionLotMapper::toActivityResponse)
                .toList();
    }

    private InternalUserEntity resolveUser(Long userId) {
        if (userId != null && userId > 0) {
            return internalUserRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Internal user not found"));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof CredentialsEntity creds && creds.getUsuario() != null) {
                return creds.getUsuario();
            }
            String username = auth.getName();
            if (username != null) {
                CredentialsEntity creds = credentialsRepository.findByIdentifier(username).orElse(null);
                if (creds != null && creds.getUsuario() != null) {
                    return creds.getUsuario();
                }
            }
        }

        return internalUserRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Internal user not found"));
    }
}
