package com.stretto.demo.features.flavors;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlavorsRepository extends JpaRepository<FlavorsEntity, Long> {
}
