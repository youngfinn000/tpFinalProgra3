package com.stretto.demo.auth.credentials;

import com.stretto.demo.common.exception.NotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws NotFoundException {
        return credentialsRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
