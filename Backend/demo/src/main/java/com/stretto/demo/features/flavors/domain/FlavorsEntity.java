package com.stretto.demo.features.flavors.domain;

import com.stretto.demo.features.stock.domain.StockEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "flavors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlavorsEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean activeInactive;

}
