package com.stretto.demo.features.wholesaleCustomer;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WholesaleCustomerRepository extends JpaRepository<WholesaleCustomerEntity, Long> {


    Optional<WholesaleCustomerEntity> findByEmail(String email);
    List<WholesaleCustomerEntity> findByActiveTrue();
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByCuitAndIdNot(String cuit, Long id);
}
