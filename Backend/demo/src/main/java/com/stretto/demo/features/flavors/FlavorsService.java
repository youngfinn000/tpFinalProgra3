package com.stretto.demo.features.flavors;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTORequest;
import com.stretto.demo.features.flavors.domain.dto.FlavorsDTOResponse;

import java.util.List;

public interface FlavorsService {

    FlavorsDTOResponse create (FlavorsDTORequest request);

    List<FlavorsDTOResponse> findAll();

    FlavorsDTOResponse findById(Long id);

    FlavorsDTOResponse update(Long id, FlavorsDTORequest request);

    void delete (Long id);

    FlavorsDTOResponse activate(Long id);

    FlavorsDTOResponse findByName (String name);

    List<FlavorsDTOResponse> findInactive();

    List<FlavorsDTOResponse> searchByName(String name);
}
