package com.stretto.demo.auth.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stretto.demo.common.exception.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = switch (authException) {
            case BadCredentialsException badCredentialsException ->
                    "Credenciales inválidas";
            case DisabledException disabledException ->
                    "Cuenta deshabilitada";
            case LockedException lockedException ->
                    "Cuenta bloqueada";
            case AccountExpiredException accountExpiredException ->
                    "Cuenta expirada";
            case CredentialsExpiredException credentialsExpiredException ->
                    "Credenciales expiradas";
            case InsufficientAuthenticationException insufficientAuthenticationException ->
                    "Autenticación insuficiente";
            case AuthenticationServiceException authenticationServiceException ->
                    "Error en el servicio de autenticación";
            default -> "Error de autenticación: " + authException.getMessage();
        };

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", errorMessage);
        responseData.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        responseData.put("path", request.getRequestURI());

        response.getWriter().write(mapper.writeValueAsString(responseData));

        response.getWriter().flush();
    }
}

