package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductionLotRepository extends JpaRepository<ProductionLotEntity, Long> {
    List<ProductionLotEntity> findByProductionDate(LocalDate date); //Listar por fecha
}
