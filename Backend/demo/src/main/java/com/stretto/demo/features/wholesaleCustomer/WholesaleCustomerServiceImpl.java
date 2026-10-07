package com.stretto.demo.features.wholesaleCustomer;

import com.stretto.demo.auth.credentials.CredentialsEntity;
import com.stretto.demo.auth.credentials.CredentialsRepository;
import com.stretto.demo.auth.permissions.RoleEntity;
import com.stretto.demo.auth.permissions.RoleRepository;
import com.stretto.demo.auth.permissions.Roles;
import com.stretto.demo.common.exception.AlreadyExistsException;
import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoRequest;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoResponse;
import com.stretto.demo.features.wholesaleCustomer.domain.mapper.WholesaleCusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class WholesaleCustomerServiceImpl implements WholesaleCustomerService {

    private final WholesaleCustomerRepository wholesaleCustomerRepository;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public WholesaleCusDtoResponse createWholesaleCustomer(WholesaleCusDtoRequest request) {
        if (wholesaleCustomerRepository.findByEmail(request.getEmail()).isPresent()
                || credentialsRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new AlreadyExistsException("Already exists Whole Sale Customer with this email");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidStateException("Password is required for wholesale customer registration");
        }

        WholesaleCustomerEntity entity = WholesaleCusMapper.toEntity(request);
        WholesaleCustomerEntity savedCustomer = wholesaleCustomerRepository.save(entity);

        RoleEntity roleWholesale = roleRepository.findByRole(Roles.ROLE_WHOLESALE)
                .orElseGet(() -> roleRepository.save(new RoleEntity(Roles.ROLE_WHOLESALE)));

        CredentialsEntity credentials = CredentialsEntity.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .wholesaleCustomer(savedCustomer)
                .roles(new HashSet<>(Set.of(roleWholesale)))
                .build();
        credentialsRepository.save(credentials);

        return WholesaleCusMapper.toResponse(savedCustomer);
    }

    @Override
    public WholesaleCusDtoResponse updateWholesaleCustomer(Long id, WholesaleCusDtoRequest request) {
        WholesaleCustomerEntity customer = findActiveOrThrow(id);
        if(wholesaleCustomerRepository.existsByEmailAndIdNot(request.getEmail(), id)){
            throw new AlreadyExistsException("There is already a customer with an email: " + request.getEmail());
        }
        if(request.getCuit() != null && wholesaleCustomerRepository.existsByCuitAndIdNot(request.getCuit(), id)){
            throw new AlreadyExistsException("There cuit is already registered for another client: "+request.getCuit());
        }
        String oldEmail = customer.getEmail();
        customer.setCompanyName(request.getCompanyName());
        customer.setEmail(request.getEmail());
        customer.setContactName(request.getContactName());
        customer.setCuit(request.getCuit());
        WholesaleCustomerEntity updatedCustomer = wholesaleCustomerRepository.save(customer);

        credentialsRepository.findByUsername(oldEmail).ifPresent(creds -> {
            if (!oldEmail.equals(request.getEmail())) {
                creds.setUsername(request.getEmail());
            }
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                creds.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            credentialsRepository.save(creds);
        });

        return WholesaleCusMapper.toResponse(updatedCustomer);
    }

    @Override
    public void deleteWholesaleCustomer(Long id) {
        WholesaleCustomerEntity customer = findActiveOrThrow(id);
        customer.setActive(false);
        wholesaleCustomerRepository.save(customer);

        credentialsRepository.findByUsername(customer.getEmail())
                .ifPresent(creds -> {
                    creds.setEnabled(false);
                    credentialsRepository.save(creds);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleCusDtoResponse getWholesaleCustomerById(Long id) {
        WholesaleCustomerEntity customer= wholesaleCustomerRepository.findById(id).orElseThrow(()->new NotFoundException("WholesaleCustomer not found with id: "+id));
        return WholesaleCusMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleCusDtoResponse> getAllWholesaleCustomer(){
        return wholesaleCustomerRepository.findByActiveTrue()
                .stream()
                .map(WholesaleCusMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleCusDtoResponse getWholesaleCustomerByEmail(String email) {
        WholesaleCustomerEntity customer = wholesaleCustomerRepository.findByEmail(email).orElseThrow(()->new NotFoundException("WholesaleCustomer not found with email: "+email));
        return WholesaleCusMapper.toResponse(customer);
    }

    private WholesaleCustomerEntity findActiveOrThrow(Long id) {
        WholesaleCustomerEntity customer= wholesaleCustomerRepository.findById(id).orElseThrow(()->new NotFoundException("WholesaleCustomer not found with id: "+id));
        if(!customer.isActive()){
            throw new InvalidStateException("Wholesale Customer with id "+id+ " its inactive. " );
        }
        return customer;
    }
}
