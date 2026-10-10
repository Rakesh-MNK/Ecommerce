package com.ecommerce.gateway.config;

import com.ecommerce.gateway.security.JwtGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProtectedRouteConfig {

    @Bean
    RouteLocator protectedRoutes(RouteLocatorBuilder builder, JwtGatewayFilter jwtGatewayFilter) {
        return builder.routes()
                .route("protected-eureka-status-route", route -> route
                        .path("/api/protected/eureka/status")
                        .filters(filters -> filters
                                .filter(jwtGatewayFilter::filter)
                                .rewritePath("/api/protected/eureka/status", "/api/eureka/status"))
                        .uri("http://localhost:8761"))
                .build();
    }
}
