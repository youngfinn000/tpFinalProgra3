package com.stretto.demo.features.internalUser;


import com.stretto.demo.common.exception.InvalidRoleException;
import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import com.stretto.demo.features.internalUser.domain.enums.RolEnum;
import com.stretto.demo.features.internalUser.domain.mapper.InternalUserMapper;
import com.stretto.demo.features.order.OrderService;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.productionLot.ProductionLotService;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalUserImpl implements InternalUserService {

    private final InternalUserRepository repository;
    private final OrderService orderService;
    private final ProductionLotService productionLotService;

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
                .orElseThrow(() -> new NotFoundException("User not found"));

        return InternalUserMapper.toResponse(entity);
    }

    @Override
    public InternalUserDTOResponse update(Long id, InternalUserDTORequest request)
    {
        InternalUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setRol(request.getRol());
        InternalUserEntity updated = repository.save(entity);
        return InternalUserMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id){
        InternalUserEntity entity = repository.findById(id)

                .orElseThrow(() -> new NotFoundException("User not found"));
        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public InternalUserDTOResponse activate(Long id){
        InternalUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(entity.isActive()){
            throw new InvalidStateException("User is already active");
        }

        entity.setActive(true);
        InternalUserEntity updated = repository.save(entity);
        return InternalUserMapper.toResponse(updated);
    }

    @Override
    public OrderDTOResponse createOrder(Long userId, OrderDTORequest request) {
        InternalUserEntity entity = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(entity.getRol() != RolEnum.EMPLOYEE){
            throw new InvalidRoleException("Only employees can perform this action");
        }

        return orderService.create(request);
    }

    @Override
    public ProductionLotDTOResponse registerLot(Long userId, ProductionLotDTORequest request) {
        InternalUserEntity entity = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(entity.getRol() != RolEnum.EMPLOYEE){
            throw new InvalidRoleException("Only employees can perform this action");
        }

        return productionLotService.create(request);
    }
}
