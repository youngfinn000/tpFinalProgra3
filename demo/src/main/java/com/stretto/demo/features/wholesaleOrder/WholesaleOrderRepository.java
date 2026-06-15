package com.stretto.demo.features.wholesaleOrder;

import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WholesaleOrderRepository extends JpaRepository<WholesaleOrderEntity , Long>{

    Optional<WholesaleOrderEntity> findByRequestBudgetId(Long rbId);
    List<WholesaleOrderEntity> findByActiveTrue();
    List<WholesaleOrderEntity> findByWholesaleCustomerIdAndActiveTrue(Long id);
}
