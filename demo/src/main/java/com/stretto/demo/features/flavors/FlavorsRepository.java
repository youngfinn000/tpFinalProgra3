package com.stretto.demo.features.flavors;

import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlavorsRepository extends JpaRepository<FlavorsEntity, Long> {

    List<FlavorsEntity> findByActiveInactiveTrue();

    boolean existsByNameIgnoreCase (String name);

    Optional<FlavorsEntity> findByNameIgnoreCase(String name) ;

    List<FlavorsEntity> findByActiveInactiveFalse();

    List<FlavorsEntity> findByNameContainingIgnoreCase(String name);
}
