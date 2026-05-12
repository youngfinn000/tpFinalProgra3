package com.stretto.demo.features.flavors.domain.mapper;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTORequest;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTOResponse;

public class FlavorsMapper {

    //Entity -> DTO //devuelve
    public static FlavorsDTOResponse toResponse (FlavorsEntity entity)
    {
        return FlavorsDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active_inactive(entity.isActive_inactive())
                .build();
    }

    //DTO -> Entity // guarda
    public static FlavorsEntity toEntity (FlavorsDTORequest request)
    {
        return FlavorsEntity.builder()
                .name(request.getName())
                .active_inactive(true)
                .build();
    }
}
