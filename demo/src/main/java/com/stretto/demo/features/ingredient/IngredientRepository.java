package com.stretto.demo.features.ingredient;

import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository  extends JpaRepository <IngredientEntity,Long> {
}
