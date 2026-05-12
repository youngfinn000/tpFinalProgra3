package com.stretto.demo.features.flavors.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlavorsDTOResponse {

    private Long id;
    private String name;
    private boolean active_inactive;
}
