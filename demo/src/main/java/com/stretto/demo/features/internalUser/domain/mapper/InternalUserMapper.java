package com.stretto.demo.features.internalUser.domain.mapper;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;

//El Mapper convierte entre Entity ↔ DTO. Es una clase con métodos estáticos, sin anotaciones especiales.
public class InternalUserMapper {

    public static InternalUserDTOResponse toResponse(InternalUserEntity entity)
    {
        return InternalUserDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .rol(entity.getRol())
                .active(entity.isActive())
                .build();
    }

    public static InternalUserEntity toEntity(InternalUserDTORequest request)
    {
        return InternalUserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(request.getPasswordHash())
                .rol(request.getRol())
                .active(true)
                .build();
    }
}
