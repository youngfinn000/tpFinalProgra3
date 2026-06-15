package com.stretto.demo.features.wholesaleOrder;

import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WholesaleOrderRepository extends JpaRepository<WholesaleOrderEntity , Long>{

    List<WholesaleOrderEntity> findByWholesaleCustomerIdAndActiveTrue(Long customerId);
    List<WholesaleOrderEntity> findByActiveTrue();
    Optional<WholesaleOrderEntity> findByRequestBudgetId(Long requestBudgetId);


}
