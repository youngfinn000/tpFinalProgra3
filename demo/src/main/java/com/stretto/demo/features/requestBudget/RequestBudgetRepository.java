package com.stretto.demo.features.requestBudget;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.requestBudget.domain.enums.StateRequestEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestBudgetRepository extends JpaRepository<RequestBudgetEntity, Long> {

    // Todas las solicitudes de un cliente
    List<RequestBudgetEntity> findByCustomerId(Long customerId);

    // Solicitudes filtradas por estado (para el admin)
    List<RequestBudgetEntity> findByStateRequestEnum(StateRequestEnum state);

    // Solicitudes de un cliente filtradas por estado
    List<RequestBudgetEntity> findByCustomerIdAndStateRequestEnum(Long customerId, StateRequestEnum state);
}
