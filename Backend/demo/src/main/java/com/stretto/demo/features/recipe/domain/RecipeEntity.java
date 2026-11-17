package com.stretto.demo.features.recipe.domain;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.productionLot.domain.ProductionLotEntity;
import com.stretto.demo.features.recipe.domain.enums.UnitMeasurementEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double baseQuantity;

    @Enumerated(EnumType.STRING)
    private UnitMeasurementEnum unitBase;

    @ManyToOne
    @JoinColumn(name = "flavor_id")
    private FlavorsEntity flavor;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "production_lot_id")
    private ProductionLotEntity productionLot;

    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientEntity> ingredients = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "recipe_subrecipe",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_recipe_id")
    )
    private List<RecipeEntity> subRecipes = new ArrayList<>();
}
