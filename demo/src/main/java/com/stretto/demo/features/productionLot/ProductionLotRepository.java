package com.stretto.demo.features.productionLot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionLotRepository extends JpaRepository<ProductionLotRepository, Long> {
}
