package com.stretto.demo.features.stock.domain;

import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import com.stretto.demo.features.stock.domain.enums.StatusEnum;
import com.stretto.demo.features.stockModification.domain.StockModificationEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double currentStock;

    @Column(nullable = false)
    private Double minimumStock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitMeasurementEnum unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @OneToOne(mappedBy = "stock")
    private StockModificationEntity stockModification;
}
