package com.stretto.demo.features.flavors;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTORequest;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTOResponse;
import com.stretto.demo.features.flavors.domain.mapper.FlavorsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlavorsServiceImpl implements FlavorsService{

    private final FlavorsRepository flavorsRepository;

    @Override
    public FlavorsDTOResponse create (FlavorsDTORequest request)
    {
        if (flavorsRepository.existsByName(request.getName()))
        {
            throw new RuntimeException("A flavor with that name already exists");
        }
        FlavorsEntity entity = FlavorsMapper.toEntity(request);
        FlavorsEntity saved = flavorsRepository.save(entity);
        return FlavorsMapper.toResponse(saved);
    }

    @Override
    public List<FlavorsDTOResponse> findAll()
    {
        return flavorsRepository.findByActive_inactiveTrue()
                .stream()
                .map(FlavorsMapper :: toResponse)
                .toList();
    }

    @Override
    public FlavorsDTOResponse findById (Long id)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flavor not found with id: " + id));
        return FlavorsMapper.toResponse((entity));
    }

    @Override
    public FlavorsDTOResponse update (Long id, FlavorsDTORequest request)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flavor not found with id: " + id));
        entity.setName(request.getName());
        FlavorsEntity updated = flavorsRepository.save(entity);
        return FlavorsMapper.toResponse(updated);
    }

    @Override
    public void delete (Long id)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flavor not found with id: " + id));
        entity.setActive_inactive(false);
        flavorsRepository.save(entity);
    }

    @Override
    public FlavorsDTOResponse activate (Long id)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flavor not found with id: " + id));
        entity.setActive_inactive(true);
        FlavorsEntity updated = flavorsRepository.save(entity);
        return FlavorsMapper.toResponse(updated);
    }

}
