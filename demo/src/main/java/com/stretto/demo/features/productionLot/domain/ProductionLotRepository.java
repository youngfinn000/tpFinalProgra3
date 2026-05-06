package com.stretto.demo.features.productionLot.domain;

import com.stretto.demo.features.recipe.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionLotRepository extends JpaRepository<ProductionLotRepository, Long> {
}
