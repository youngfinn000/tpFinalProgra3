package com.stretto.demo.features.internalUser;

import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//Es una interfaz que extiende JpaRepository. Spring genera la implementación automáticamente, vos solo declarás los métodos que necesitás.
public interface InternalUserRepository extends JpaRepository<InternalUserEntity, Long> {
}

//Agregar los metodos de esta clase