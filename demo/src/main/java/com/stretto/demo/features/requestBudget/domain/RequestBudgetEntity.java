package com.stretto.demo.features.requestBudget.domain;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.requestBudget.domain.enums.StateRequestEnum;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "request_budgets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBudgetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private int  quantityKg;

    @Column (nullable = false)
    private LocalDateTime requestDateTime;

    @Column (nullable = false)
    private double budget;

    @Column (nullable = false)
    private boolean advancePayment;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private StateRequestEnum stateRequestEnum;

    //solicitud de presupuesto - cliente mayorista (manytoOne)
    @ManyToOne
    @JoinColumn (name = "wholesale_customer_id")
    private WholesaleCustomerEntity customer;

    //solicitud de presupuesto - pedido mayorista (onetoOne) UNIDIRECCIONAL

    //solicitud de presupuesto - sabores (manytomany)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable (name = "requestbudget_flavors",
    joinColumns = @JoinColumn (name = "request_budget_id"),
            inverseJoinColumns = @JoinColumn (name = "flavors_id"))
    private List<FlavorsEntity> listflavors;

}
