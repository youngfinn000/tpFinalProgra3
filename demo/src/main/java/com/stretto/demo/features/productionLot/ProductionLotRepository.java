package com.stretto.demo.features.productionLot;

import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductionLotRepository extends JpaRepository<ProductionLotEntity, Long> {
}
