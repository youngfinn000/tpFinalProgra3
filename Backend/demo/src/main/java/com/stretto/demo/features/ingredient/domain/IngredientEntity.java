package com.stretto.demo.features.ingredient.domain;

import com.stretto.demo.features.recipe.domain.RecipeEntity;
import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import com.stretto.demo.features.stock.domain.StockEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double requiredAmount;

    @Enumerated(EnumType.STRING)
    private UnitMeasurementEnum unit;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @OneToOne
    @JoinColumn(name = "stock_id")
    private StockEntity stock;
}
