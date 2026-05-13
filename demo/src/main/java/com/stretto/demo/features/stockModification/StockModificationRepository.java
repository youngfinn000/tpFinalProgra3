package com.stretto.demo.features.stockModification;

import com.stretto.demo.features.stockModification.domain.StockModificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockModificationRepository extends JpaRepository<StockModificationEntity, Long> {
}
