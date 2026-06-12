package com.stretto.demo.features.product;

import com.stretto.demo.features.product.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByActiveTrue();

    boolean existsByName(String name);

    List<ProductEntity> findByFlavors_Id(Long flavorId);
}
