package com.stretto.demo.features.ingredient.domain;

import com.stretto.demo.features.ingredient.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository  extends JpaRepository <IngredientEntity,Long> {
}
