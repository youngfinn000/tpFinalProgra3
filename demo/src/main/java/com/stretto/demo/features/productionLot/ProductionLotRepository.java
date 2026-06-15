package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductionLotRepository extends JpaRepository<ProductionLotEntity, Long> {
    List<ProductionLotEntity> findByProductionDate(LocalDate date); //Listar por fecha

    @Query("SELECT pl FROM ProductionLotEntity pl JOIN pl.recipe.product.flavors f WHERE f.id = :flavorId")
    List<ProductionLotEntity> findByFlavorId(@Param("flavorId") Long flavorId); //Listar por sabor

    @Query("SELECT pl FROM ProductionLotEntity pl JOIN pl.recipe.product.flavors f WHERE pl.productionDate = :date AND f.id = :flavorId")
    List<ProductionLotEntity> findByProductionDateAndFlavorId(@Param("date") LocalDate date, @Param("flavorId") Long flavorId); //Listar por sabor y fecha
}
