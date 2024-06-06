package com.techworld.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String ADMIN = "admin";
    private static final String USER = "user";
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange()

                // Public endpoints
                    .pathMatchers("/auth/users/**").permitAll()
                    .pathMatchers("/auth/roles/**").permitAll()

                // User role endpoints
                    .pathMatchers("/flight-search-service/v1/api/search/**").hasRole(USER)
                    .pathMatchers(HttpMethod.GET, "/flight-service/v1/api/flights/**").hasRole(USER)
                    .pathMatchers(HttpMethod.PUT, "/flight-service/v1/api/flights/**").hasRole(USER)
                    .pathMatchers("/booking-service/v1/api/flights/**").hasRole(USER)

                // Admin role endpoints
                    .pathMatchers(HttpMethod.POST, "/flight-service/v1/api/flights/**").hasRole(ADMIN)

                // Any other request must be authenticated
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);

        return http.build();
    }
}
