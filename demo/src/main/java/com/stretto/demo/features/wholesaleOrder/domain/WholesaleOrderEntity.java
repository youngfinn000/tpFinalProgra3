package com.stretto.demo.features.wholesaleOrder.domain;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table (name = "wholesale_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WholesaleOrderEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private boolean advancePayment;

    private LocalDate deliveryDate;

    private double discount;

    //pedido mayorista - pedido (oneToOne)
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "order_id")
    private OrderEntity order;

    //pedido mayorista - solicitud (onetoOne)
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "request_budget_id")
    private RequestBudgetEntity requestBudget;

    //pedido mayorista - cliente mayorista (manytoOne)
    @ManyToOne
    @JoinColumn (name = "wholesale_customer_id")
    private WholesaleCustomerEntity wholesaleCustomer;


}
