package com.stretto.demo.features.productionLot.domain;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.productionLot.domain.enums.StatusLotEnum;
import com.stretto.demo.features.recipe.RecipeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "production_lot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductionLotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amountProduced;

    @Enumerated(EnumType.STRING)
    private StatusLotEnum status;

    private Double performancePct;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private InternalUserEntity internalUser;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;
}
