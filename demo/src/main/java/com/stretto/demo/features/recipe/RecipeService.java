package com.stretto.demo.features.recipe;

import com.stretto.demo.features.recipe.domain.dto.RecipeDTORequest;
import com.stretto.demo.features.recipe.domain.dto.RecipeDTOResponse;
import com.stretto.demo.features.recipe.domain.dto.RecipeSuppliesDTOResponse;

import java.util.List;

public interface RecipeService {

    RecipeDTOResponse create(RecipeDTORequest request);

    List<RecipeDTOResponse> findAll();

    RecipeDTOResponse findById(Long id);

    RecipeDTOResponse update(RecipeDTORequest request, Long id);

    List<RecipeSuppliesDTOResponse> calculateIngredients(Long recipeId, Double kg);
}
