package com.stretto.demo.features.internalUser;

import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;


import java.util.List;

public interface InternalUserService {

    InternalUserDTOResponse create(InternalUserDTORequest request);

    List<InternalUserDTOResponse> findAll();

    InternalUserDTOResponse findById(Long id);

    InternalUserDTOResponse update(Long id, InternalUserDTORequest request);

    void delete(Long id);

    InternalUserDTOResponse activate(Long id);
}
