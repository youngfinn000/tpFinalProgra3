package com.stretto.demo.features.stock.domain;

import com.stretto.demo.features.recipe.RecipeEntity;
import com.stretto.demo.features.stock.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
}
