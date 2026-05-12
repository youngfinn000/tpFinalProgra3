package com.stretto.demo.features.internalUser.domain.dto;

import com.stretto.demo.features.internalUser.domain.enums.RolEnum;
import lombok.*;

//Lo que la API devuelve al cliente
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternalUserDTOResponse {
    private Long id;
    private String name;
    private String email;
    private RolEnum rol;
    private boolean active;
}
