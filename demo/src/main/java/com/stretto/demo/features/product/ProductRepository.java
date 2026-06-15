package com.stretto.demo.features.product;

import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByActiveTrue();

    boolean existsByNameIgnoreCase(String name);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    List<ProductEntity> findByStockLessThanEqual (Integer stock);

    List<ProductEntity> findByStockGreaterThan (Integer stock);

}
