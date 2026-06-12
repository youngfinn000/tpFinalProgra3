package com.stretto.demo.features.flavors.domain;

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

    private boolean active_inactive;

    //RELACION A DISTANCIA CON LA TABLA PRODUCTOS (UNIDIRECCIONAL)






}
