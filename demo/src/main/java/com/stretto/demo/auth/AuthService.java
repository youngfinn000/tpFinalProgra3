package com.stretto.demo.auth;

import com.stretto.demo.auth.credentials.CredentialsEntity;
import com.stretto.demo.auth.credentials.CredentialsRepository;
import com.stretto.demo.auth.dtos.AuthRequest;
import com.stretto.demo.auth.dtos.NewAccountRequest;
import com.stretto.demo.auth.permissions.RoleEntity;
import com.stretto.demo.auth.permissions.RoleRepository;
import com.stretto.demo.auth.permissions.Roles;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import com.stretto.demo.features.internalUser.domain.enums.RolEnum;
import com.stretto.demo.features.internalUser.domain.mapper.InternalUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CredentialsRepository credentialsRepository;
    private final InternalUserRepository internalUserRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserDetails authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );
        return credentialsRepository.findByUsername(input.username()).orElseThrow(
                () -> new NotFoundException("Usuario no encontrado")
        );
    }

    @Transactional
    public InternalUserDTOResponse register(NewAccountRequest request) {
        InternalUserEntity userEntity = new InternalUserEntity();
        userEntity.setName(request.name());
        userEntity.setEmail(request.email());
        userEntity.setPasswordHash(passwordEncoder.encode(request.password()));
        userEntity.setActive(true);
        userEntity.setRol(RolEnum.EMPLOYEE);
        InternalUserEntity savedUser = internalUserRepository.save(userEntity);

        RoleEntity roleUser = roleRepository.findByRole(Roles.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Rol ROLE_USER no encontrado"));

        CredentialsEntity credentials = new CredentialsEntity();
        credentials.setUsername(request.username());
        credentials.setPassword(passwordEncoder.encode(request.password()));
        credentials.setEnabled(true);
        credentials.setUsuario(savedUser);
        credentials.getRoles().add(roleUser);
        credentialsRepository.save(credentials);

        return InternalUserMapper.toResponse(savedUser);
    }
}
