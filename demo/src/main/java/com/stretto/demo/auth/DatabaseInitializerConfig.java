package com.stretto.demo.auth;

import com.stretto.demo.auth.credentials.CredentialsEntity;
import com.stretto.demo.auth.credentials.CredentialsRepository;
import com.stretto.demo.auth.permissions.*;
import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.internalUser.domain.enums.RolEnum;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseInitializerConfig {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            PermitRepository permitRepository,
            RoleRepository roleRepository,
            CredentialsRepository credentialsRepository,
            InternalUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (permitRepository.count() > 0) return;

            PermitEntity crearUsuario = permitRepository.save(PermitEntity.builder().permit(Permit.CREAR_USUARIO).build());
            PermitEntity actualizarCuenta = permitRepository.save(PermitEntity.builder().permit(Permit.ACTUALIZAR_CUENTA).build());
            PermitEntity eliminarUsuario = permitRepository.save(PermitEntity.builder().permit(Permit.ELIMINAR_USUARIO).build());

            RoleEntity roleUser = new RoleEntity(Roles.ROLE_USER);
            roleUser.getPermits().add(actualizarCuenta);
            roleRepository.save(roleUser);

            RoleEntity roleAdmin = new RoleEntity(Roles.ROLE_ADMIN);
            roleAdmin.getPermits().add(crearUsuario);
            roleAdmin.getPermits().add(actualizarCuenta);
            roleAdmin.getPermits().add(eliminarUsuario);
            roleRepository.save(roleAdmin);

            String passwordEncriptada = passwordEncoder.encode("password123");

            InternalUserEntity adminUser = new InternalUserEntity();
            adminUser.setName("Admin");
            adminUser.setEmail("admin@stretto.com");
            adminUser.setPasswordHash(passwordEncriptada);
            adminUser.setActive(true);
            adminUser.setRol(RolEnum.ADMIN);
            userRepository.save(adminUser);

            CredentialsEntity adminCreds = new CredentialsEntity();
            adminCreds.setUsername("admin");
            adminCreds.setPassword(passwordEncriptada);
            adminCreds.setEnabled(true);
            adminCreds.setUsuario(adminUser);
            adminCreds.getRoles().add(roleAdmin);
            credentialsRepository.save(adminCreds);

            InternalUserEntity employeeUser = new InternalUserEntity();
            employeeUser.setName("Empleado");
            employeeUser.setEmail("empleado@stretto.com");
            employeeUser.setPasswordHash(passwordEncriptada);
            employeeUser.setActive(true);
            employeeUser.setRol(RolEnum.EMPLOYEE);
            userRepository.save(employeeUser);

            CredentialsEntity employeeCreds = new CredentialsEntity();
            employeeCreds.setUsername("empleado");
            employeeCreds.setPassword(passwordEncriptada);
            employeeCreds.setEnabled(true);
            employeeCreds.setUsuario(employeeUser);
            employeeCreds.getRoles().add(roleUser);
            credentialsRepository.save(employeeCreds);
        };
    }
}
