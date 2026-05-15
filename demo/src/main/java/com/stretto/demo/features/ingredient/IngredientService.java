package com.stretto.demo.features.ingredient;

import com.stretto.demo.features.ingredient.domain.dto.IngredientDTORequest;
import com.stretto.demo.features.ingredient.domain.dto.IngredientDTOResponse;

import java.util.List;

public interface IngredientService {

    IngredientDTOResponse create(IngredientDTORequest request);

    List<IngredientDTOResponse> findAll();

    IngredientDTOResponse findById(Long id);

    IngredientDTOResponse update(IngredientDTORequest request, Long id);
}
