package com.stretto.demo.features.wholesaleOrder;

import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WholesaleOrderRepository extends JpaRepository<WholesaleOrderEntity , Long>{
}
