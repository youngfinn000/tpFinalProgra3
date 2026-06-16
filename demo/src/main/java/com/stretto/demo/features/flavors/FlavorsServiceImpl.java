package com.stretto.demo.features.flavors;

import com.stretto.demo.common.exception.AlreadyExistsException;
import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTORequest;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTOResponse;
import com.stretto.demo.features.flavors.domain.mapper.FlavorsMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlavorsServiceImpl implements FlavorsService{

    private final FlavorsRepository flavorsRepository;
    private final FlavorsMapper flavorsMapper;

    //CREAR SABOR
    @Override
    @Transactional
    public FlavorsDTOResponse create (FlavorsDTORequest request)
    {
        if (flavorsRepository.existsByNameIgnoreCase(request.getName()))
        {
            throw new AlreadyExistsException("A flavor with that name already exists");
        }

        FlavorsEntity entity = flavorsMapper.toEntity(request);
        FlavorsEntity saved = flavorsRepository.save(entity);
        return flavorsMapper.toResponse(saved);
    }

    //BUSCAR TODOS LOS SABORES ACTIVOS
    @Override
    public List<FlavorsDTOResponse> findAll()
    {
        return flavorsRepository.findByActiveInactiveTrue()
                .stream()
                .map(flavorsMapper :: toResponse)
                .toList();
    }

    //BUSCAR POR ID
    @Override
    public FlavorsDTOResponse findById (Long id)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flavor not found with id: " + id));
        return flavorsMapper.toResponse((entity));
    }

    //ACTUALIZAR SABOR
    @Override
    @Transactional
    public FlavorsDTOResponse update (Long id, FlavorsDTORequest request)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flavor not found with id: " + id));

        if(!entity.getName().equalsIgnoreCase(request.getName()) &&
                flavorsRepository.existsByNameIgnoreCase(request.getName()))
        {
            throw new AlreadyExistsException("A flavor with that name already exists");
        }

        entity.setName(request.getName());

        FlavorsEntity updated = flavorsRepository.save(entity);

        return flavorsMapper.toResponse(updated);
    }

    //DAR DE BAJA UN SABOR
    @Override
    public void delete (Long id)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flavor not found with id: " + id));

        entity.setActiveInactive(false);

        flavorsRepository.save(entity);
    }

    //DAR DE ALTA UN SABOR
    @Override
    public FlavorsDTOResponse activate (Long id)
    {
        FlavorsEntity entity = flavorsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flavor not found with id: " + id));

        if(entity.isActiveInactive()){
            throw new InvalidStateException("You can only re-enable a disabled flavor");
        }

        entity.setActiveInactive(true);

        FlavorsEntity updated = flavorsRepository.save(entity);
        return flavorsMapper.toResponse(updated);
    }


    //BUSCAR POR NOMBRE
    @Override
    public FlavorsDTOResponse findByName (String name)
    {
        FlavorsEntity entity = flavorsRepository.findByNameIgnoreCase(name)
                .orElseThrow(()-> new NotFoundException("Flavor not found: " + name));

        return flavorsMapper.toResponse(entity);
    }

    //BUSCAR SABORES INACTIVOS
    @Override
    public List<FlavorsDTOResponse> findInactive()
    {
        return flavorsRepository.findByActiveInactiveFalse()
                .stream()
                .map(flavorsMapper :: toResponse)
                .toList();
    }

    //BUSCAR POR TEXTO
    @Override
    public List<FlavorsDTOResponse> searchByName(String text)
    {
        return flavorsRepository.findByNameContainingIgnoreCase(text)
                .stream()
                .map(flavorsMapper :: toResponse)
                .toList();
    }

}
