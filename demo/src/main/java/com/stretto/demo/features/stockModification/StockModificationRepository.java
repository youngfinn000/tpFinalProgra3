package com.stretto.demo.features.stockModification;

import com.stretto.demo.features.stockModification.domain.StockModificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockModificationRepository extends JpaRepository<StockModificationEntity, Long> {
    List<StockModificationEntity> findByModificationDate(LocalDate date);
}
