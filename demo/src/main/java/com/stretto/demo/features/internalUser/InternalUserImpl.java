package com.stretto.demo.features.internalUser;


import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import com.stretto.demo.features.internalUser.domain.mapper.InternalUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalUserImpl implements InternalUserService {

    private final InternalUserRepository repository;

    @Override
    public InternalUserDTOResponse create(InternalUserDTORequest request){
        InternalUserEntity entity = InternalUserMapper.toEntity(request);
        InternalUserEntity saved = repository.save(entity);
        return InternalUserMapper.toResponse(saved);
    }

    @Override
    public List<InternalUserDTOResponse> findAll(){
        List<InternalUserEntity> entities = repository.findAll();
        return entities.stream()
                .map(InternalUserMapper::toResponse)
                .toList();
    }

    @Override
    public InternalUserDTOResponse findById(Long id)
    {
        InternalUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return InternalUserMapper.toResponse(entity);
    }

    @Override
    public InternalUserDTOResponse update(Long id, InternalUserDTORequest request)
    {
        InternalUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setRol(request.getRol());
        InternalUserEntity updated = repository.save(entity);
        return InternalUserMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id){
        InternalUserEntity entity = repository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public InternalUserDTOResponse activate(Long id){
        InternalUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setActive(true);
        InternalUserEntity updated = repository.save(entity);
        return InternalUserMapper.toResponse(updated);
    }
}
