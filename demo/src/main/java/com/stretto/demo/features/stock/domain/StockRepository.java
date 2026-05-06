package com.stretto.demo.features.stock.domain;

import com.stretto.demo.features.recipe.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockRepository, Long> {
}
