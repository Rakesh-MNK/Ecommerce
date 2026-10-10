package com.ecommerce.gateway.controller;

import com.ecommerce.gateway.security.JwtService;
import jakarta.validation.constraints.NotBlank;
//import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/gateway")
@Validated
public class GatewayController {

    private final JwtService jwtService;

    public GatewayController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/public")
    public GatewayResponse publicEndpoint() {
        return new GatewayResponse("APIGATEWAYSERVICE", "UP", "Public gateway endpoint", Instant.now());
    }

    @GetMapping("/validate-token")
    public GatewayResponse validateToken(@RequestHeader("Authorization") @NotBlank String authorization) {
        if (!authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header must use Bearer scheme");
        }
        var claims = jwtService.parse(authorization.substring(7));
        String subject = claims.getSubject() == null ? "unknown" : claims.getSubject();
        String role = claims.get("role", String.class);
        return new GatewayResponse("APIGATEWAYSERVICE", "AUTHENTICATED",
                "JWT valid for subject=" + subject + ", role=" + role, Instant.now());
    }

    public record GatewayResponse(String service, String status, String message, Instant timestamp) {
    }
}
