package com.music.catalog.infra.api.controller;

import com.music.catalog.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoint")
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Get JWT Token (Use admin/123456)")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // Simulação de autenticação (Em produção, buscaria no banco de dados users)
        if ("admin".equals(request.username()) && "123456".equals(request.password())) {
            String token = tokenService.generateToken(request.username());
            return ResponseEntity.ok(new TokenResponse(token));
        }
        return ResponseEntity.status(401).build();
    }

    public record LoginRequest(String username, String password) {}
    public record TokenResponse(String token) {}
}