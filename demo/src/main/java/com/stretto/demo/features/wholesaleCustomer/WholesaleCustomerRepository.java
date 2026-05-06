package com.stretto.demo.features.wholesaleCustomer;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WholesaleCustomerRepository extends JpaRepository<WholesaleCustomerEntity, Long> {
}
