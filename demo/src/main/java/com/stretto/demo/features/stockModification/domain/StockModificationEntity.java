package com.stretto.demo.features.stockModification.domain;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.stock.StockEntity;
import com.stretto.demo.features.stockModification.domain.enums.AdjustmentTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "stock_modification_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockModificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AdjustmentTypeEnum adjustmentType;

    private Double amount;

    private String motive;

    @Column(nullable = false)
    private LocalDate modificationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private InternalUserEntity internalUser;

    @OneToOne
    @JoinColumn(name = "stock_id")
    private StockEntity stock;
}
