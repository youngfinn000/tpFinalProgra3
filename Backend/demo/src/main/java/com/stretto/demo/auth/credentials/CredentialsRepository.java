package com.stretto.demo.auth.credentials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity,Long> {

    Optional<CredentialsEntity> findByUsername(String username);

    Optional<CredentialsEntity> findByUsuarioId(Long usuarioId);

    @Query("SELECT c FROM CredentialsEntity c LEFT JOIN c.usuario u LEFT JOIN c.wholesaleCustomer wc " +
           "WHERE LOWER(c.username) = LOWER(:identifier) " +
           "OR (u IS NOT NULL AND LOWER(u.email) = LOWER(:identifier)) " +
           "OR (wc IS NOT NULL AND LOWER(wc.email) = LOWER(:identifier))")
    Optional<CredentialsEntity> findByIdentifier(@Param("identifier") String identifier);
}
