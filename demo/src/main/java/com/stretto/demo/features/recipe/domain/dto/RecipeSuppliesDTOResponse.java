package com.stretto.demo.features.recipe.domain.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSuppliesDTOResponse {
    private Long id;
    private Double requiredQuantity;
    private String ingredientName;
}
