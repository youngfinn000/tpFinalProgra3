package com.stretto.demo.features.internalUser;

import com.stretto.demo.auth.credentials.CredentialsEntity;
import com.stretto.demo.auth.credentials.CredentialsRepository;
import com.stretto.demo.auth.permissions.RoleEntity;
import com.stretto.demo.auth.permissions.RoleRepository;
import com.stretto.demo.auth.permissions.Roles;
import com.stretto.demo.common.exception.AlreadyExistsException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class InternalUserImpl implements InternalUserService {

    private final InternalUserRepository repository;
    private final OrderService orderService;
    private final ProductionLotService productionLotService;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public InternalUserDTOResponse create(InternalUserDTORequest request){
        if (repository.findByEmail(request.getEmail().trim()).isPresent()) {
            throw new AlreadyExistsException("Ya existe un usuario con el email: " + request.getEmail());
        }

        String rawPassword = request.getPasswordHash();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new InvalidStateException("Password is required");
        }
        String encryptedPassword = passwordEncoder.encode(rawPassword.trim());

        InternalUserEntity entity = InternalUserMapper.toEntity(request);
        entity.setEmail(request.getEmail().trim());
        entity.setName(request.getName().trim());
        entity.setPasswordHash(encryptedPassword);
        entity.setActive(true);
        InternalUserEntity saved = repository.save(entity);

        // Determinamos el rol según request.getRol()
        Roles targetRole = (request.getRol() == RolEnum.ADMIN) ? Roles.ROLE_ADMIN : Roles.ROLE_USER;
        RoleEntity roleEntity = roleRepository.findByRole(targetRole)
                .orElseGet(() -> roleRepository.save(new RoleEntity(targetRole)));

        // Generamos el username base a partir del nombre o email
        String usernameCandidate = request.getName().trim().toLowerCase().replaceAll("\\s+", "");
        if (usernameCandidate.isBlank() || credentialsRepository.findByUsername(usernameCandidate).isPresent()) {
            usernameCandidate = request.getEmail().trim().toLowerCase();
        }

        CredentialsEntity credentials = CredentialsEntity.builder()
                .username(usernameCandidate)
                .password(encryptedPassword)
                .enabled(true)
                .usuario(saved)
                .roles(new HashSet<>(Set.of(roleEntity)))
                .build();
        credentialsRepository.save(credentials);

        return InternalUserMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InternalUserDTOResponse> findAll(){
        List<InternalUserEntity> entities = repository.findAll();
        return entities.stream()
                .map(InternalUserMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
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

        entity.setName(request.getName().trim());
        entity.setEmail(request.getEmail().trim());
        entity.setRol(request.getRol());

        if (request.getPasswordHash() != null && !request.getPasswordHash().isBlank() && !request.getPasswordHash().equals("dummyPassword123")) {
            String encrypted = passwordEncoder.encode(request.getPasswordHash().trim());
            entity.setPasswordHash(encrypted);
            credentialsRepository.findByUsuarioId(entity.getId()).ifPresent(c -> {
                c.setPassword(encrypted);
                credentialsRepository.save(c);
            });
        }

        credentialsRepository.findByUsuarioId(entity.getId()).ifPresent(c -> {
            Roles targetRole = (request.getRol() == RolEnum.ADMIN) ? Roles.ROLE_ADMIN : Roles.ROLE_USER;
            RoleEntity roleEntity = roleRepository.findByRole(targetRole)
                    .orElseGet(() -> roleRepository.save(new RoleEntity(targetRole)));
            c.setRoles(new HashSet<>(Set.of(roleEntity)));
            credentialsRepository.save(c);
        });

        InternalUserEntity updated = repository.save(entity);
        return InternalUserMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id){
        InternalUserEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        entity.setActive(false);
        repository.save(entity);

        credentialsRepository.findByUsuarioId(id).ifPresent(c -> {
            c.setEnabled(false);
            credentialsRepository.save(c);
        });
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

        credentialsRepository.findByUsuarioId(id).ifPresent(c -> {
            c.setEnabled(true);
            credentialsRepository.save(c);
        });

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
