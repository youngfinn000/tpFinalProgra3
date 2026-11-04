package com.stretto.demo.features.internalUser;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternalUserRepository extends JpaRepository<InternalUserEntity, Long> {
    Optional<InternalUserEntity> findByEmail(String email);
}
