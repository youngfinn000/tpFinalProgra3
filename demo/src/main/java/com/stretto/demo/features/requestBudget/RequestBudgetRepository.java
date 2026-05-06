package com.stretto.demo.features.requestBudget;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestBudgetRepository extends JpaRepository<RequestBudgetEntity, Long> {
}
