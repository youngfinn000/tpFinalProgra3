package com.stretto.demo.auth.permissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermitRepository extends JpaRepository<PermitEntity, Long> {
}
