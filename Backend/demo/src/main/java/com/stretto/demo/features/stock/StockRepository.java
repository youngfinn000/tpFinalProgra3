package com.stretto.demo.features.stock;

import com.stretto.demo.features.stock.domain.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    @Query("SELECT s FROM StockEntity s WHERE  s.currentStock <= s.minimumStock")
    List<StockEntity> findAllLowStock();
}
