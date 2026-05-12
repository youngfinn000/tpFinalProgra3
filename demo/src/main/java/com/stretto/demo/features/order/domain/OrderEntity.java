package com.stretto.demo.features.order.domain;

import com.stretto.demo.features.order.domain.enums.PaymentMethodEnum;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import com.stretto.demo.features.product.domain.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table (name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StateOrderEnum stateOrderEnum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleChannelEnum saleChannelEnum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethodEnum paymentMethodEnum;

    private BigDecimal total;

    private LocalDateTime date;

    //pedidos - productos
    @ManyToMany
    @JoinTable (name = "order_product" ,
            joinColumns = @JoinColumn(name = "order_id") ,
            inverseJoinColumns = @JoinColumn (name = "product_id"))
    private List<ProductEntity> listProduct;


    //pedidos - usuario
    @ManyToOne
    @JoinColumn(name = "user_id")
    private InternalUser internalUser;

    //pedidos - pedidos mayoristas (OnetoOne) UNIDIRECCIONAL



}
