package com.stretto.demo.auth;

import com.stretto.demo.auth.credentials.CredentialsRepository;
import com.stretto.demo.auth.dtos.AuthRequest;
import com.stretto.demo.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;

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

}
