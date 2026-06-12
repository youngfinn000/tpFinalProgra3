package com.stretto.demo.features.recipe;

import com.stretto.demo.features.recipe.domain.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

}
