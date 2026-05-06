package com.stretto.demo.features.product.domain;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private double price;

    //productos - stock (onetoOne)
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "stock_id")
    private StockEntity stock;

    //productos- receta (onetoOne) UNIDIRECCIONAL

    //productos - pedido (many to many) UNIDIRECCIONAL

    //productos - sabores (many to many)
    @ManyToMany
    @JoinTable (name =  "product_flavors",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "flavors_id"))
    private List<FlavorsEntity> flavors;


}
