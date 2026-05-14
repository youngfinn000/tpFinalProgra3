package com.stretto.demo.features.productionLot.domain;

import com.stretto.demo.features.productionLot.domain.Enum.statusLotEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ProductionLotEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionLotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductionLotEntityId;

    // anotar receta id usuario id

    private double amountProduced;

    private statusLotEnum status;

    private double percentageReturn;


}
