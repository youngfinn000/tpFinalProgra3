package com.stretto.demo.auth.credentials;

import com.stretto.demo.common.exception.NotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws NotFoundException {
        return credentialsRepository.findByIdentifier(username.trim())
                .orElseThrow(() -> new NotFoundException("Usuario o email no encontrado: " + username));
    }
}
