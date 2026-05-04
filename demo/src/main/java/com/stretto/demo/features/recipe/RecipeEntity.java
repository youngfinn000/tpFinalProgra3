package com.stretto.demo.features.recipe;


import com.stretto.demo.features.recipe.domain.Enum.UnitMeasurement;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductId;

    private String name;

private Double baseAmount;
@Enumerated(EnumType.STRING)
private UnitMeasurement unitMeasurement;

@OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL)
    private List<Ingredients> ingredients;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductEntity> product;

    @ManyToMany
    @JoinTable(
            name="productionLot_recipe",
            joinColumns = @JoinColumn(name = "ProductId"),
            inverseJoinColumns = @JoinColumn(name = productionLotId)
    )
    private List<Lots> lot;

}
