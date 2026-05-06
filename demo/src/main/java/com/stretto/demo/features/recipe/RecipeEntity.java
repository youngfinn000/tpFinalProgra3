package com.stretto.demo.features.recipe;


import com.stretto.demo.features.product.ProductEntity;
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
    private Long RecipeId;

    private String name;

private Double baseAmount;
@Enumerated(EnumType.STRING)
private UnitMeasurement unitMeasurement;

@OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL)
    private List<Ingredients> ingredient;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductEntity> product;

    @ManyToMany
    @JoinTable(
            name="productionLot_recipe",
            joinColumns = @JoinColumn(name = "RecipeId"),
            inverseJoinColumns = @JoinColumn(name = productionLotId)
    )
    private List<Lots> lot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(Long RecipeId) {
        RecipeId = RecipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public UnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public List<Ingredients> getIngredients() {
        return ingredient;
    }

    public void setIngredients(List<Ingredients> ingredient) {
        this.ingredient = ingredient;
    }

    public List<ProductEntity> getProduct() {
        return product;
    }

    public void setProduct(List<ProductEntity> product) {
        this.product = product;
    }

    public List<Lots> getLot() {
        return lot;
    }

    public void setLot(List<Lots> lot) {
        this.lot = lot;
    }
}
