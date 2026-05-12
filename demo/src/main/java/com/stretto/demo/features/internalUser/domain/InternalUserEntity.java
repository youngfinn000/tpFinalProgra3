package com.stretto.demo.features.internalUser.domain;

//La entidad JPA que mapea la tabla en la base de datos.

import com.stretto.demo.features.internalUser.domain.enums.RolEnum;
import com.stretto.demo.features.order.domain.OrderEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "internal_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternalUserEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private RolEnum rol;

    private boolean active;

    @OneToMany (mappedBy = "internalUser")
    private List<OrderEntity> orders;

}
