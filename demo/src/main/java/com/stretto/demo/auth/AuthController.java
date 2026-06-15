package com.stretto.demo.auth;

import com.stretto.demo.auth.dtos.AuthRequest;
import com.stretto.demo.auth.dtos.AuthResponse;
import com.stretto.demo.auth.dtos.NewAccountRequest;
import com.stretto.demo.auth.jwt.JwtService;
import com.stretto.demo.features.internalUser.InternalUserService;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final InternalUserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest){
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<InternalUserDTOResponse> registerUser(@RequestBody NewAccountRequest newAccountRequest){
        return new ResponseEntity<InternalUserDTOResponse>(userService.save(newAccountRequest), HttpStatus.CREATED);
    }

}
