package com.stretto.demo.features.wholesaleCustomer.domain;

import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name ="wholesale_customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WholesaleCustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column (nullable = true)
    private String companyName;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private String contactName;

    @Column (nullable = true, unique = true)
    private String cuit;

    private boolean active;

    // cliente mayorista - solicitud presupuesto (onetomany) UNIDIRECCIONAL

    // cliente mayorista - pedido mayorista (onetomany)
    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "wholesale_customer_id")
    private List<WholesaleOrderEntity> wholesaleOrders;


}
