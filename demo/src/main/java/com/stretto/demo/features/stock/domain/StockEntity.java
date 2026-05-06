package com.stretto.demo.features.stock.domain;

import com.stretto.demo.features.ingredient.domain.IngredientEntity;
import com.stretto.demo.features.recipe.domain.Enum.UnitMeasurement;
import com.stretto.demo.features.stock.domain.Enum.StockStatusEnum;
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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private String name;

    private double currentStock;

    private double minimumStock;

    private UnitMeasurement unitMeasurement;

    private StockStatusEnum status;


    @OneToOne(mappedBy = "stock")
    private IngredientEntity ingredient ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(double currentStock) {
        this.currentStock = currentStock;
    }

    public double getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(double minimumStock) {
        this.minimumStock = minimumStock;
    }

    public UnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public StockStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StockStatusEnum status) {
        this.status = status;
    }

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }
}
