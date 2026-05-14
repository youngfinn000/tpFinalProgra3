package com.stretto.demo.features.recipe;

import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;

import java.util.List;

public interface RecipeService {

    RecipeDTOResponse create(RecipeDTORequest request);

    List<RecipeDTOResponse> findAll();

    RecipeDTOResponse findById(Long id);

    RecipeDTOResponse update(RecipeDTORequest request, Long id);
}
