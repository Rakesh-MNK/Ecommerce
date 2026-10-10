package com.ecommerce.gateway.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtGatewayFilterTest {

    @Test
    void shouldRejectMissingAuthorizationHeader() {
        JwtService jwtService = mock(JwtService.class);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        JwtGatewayFilter filter = new JwtGatewayFilter(jwtService);
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/protected/test").build());

        filter.filter(exchange, chain).block();

        assertEquals(401, exchange.getResponse().getStatusCode().value());
        verifyNoInteractions(jwtService, chain);
    }

    @Test
    void shouldContinueForValidToken() {
        JwtService jwtService = mock(JwtService.class);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        Claims claims = mock(Claims.class);
        when(jwtService.parse("token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("user-101");
        when(claims.get("role", String.class)).thenReturn("CUSTOMER");
        when(chain.filter(any())).thenReturn(Mono.empty());

        JwtGatewayFilter filter = new JwtGatewayFilter(jwtService);
        var request = MockServerHttpRequest.get("/api/protected/test")
                .header("Authorization", "Bearer token")
                .build();
        var exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(jwtService).parse("token");
        verify(chain).filter(any());
    }
}
