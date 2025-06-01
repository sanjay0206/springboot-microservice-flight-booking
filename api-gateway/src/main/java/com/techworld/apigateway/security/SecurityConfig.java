package com.techworld.apigateway.security;

import com.techworld.apigateway.security.keycloak.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/users/**").permitAll()
                        .pathMatchers("/auth/roles/**").permitAll()
                        .pathMatchers("/flight-search-service/v1/api/search/flights").hasRole(Role.USER.getRole())
                        .pathMatchers(HttpMethod.GET, "/flight-service/v1/api/flights/**").hasRole(Role.USER.getRole())
                        .pathMatchers(HttpMethod.PUT, "/flight-service/v1/api/flights/reserveSeats/**").hasRole(Role.USER.getRole())
                        .pathMatchers(HttpMethod.POST, "/booking-service/v1/api/bookings/**").hasRole(Role.USER.getRole())
                        .pathMatchers(HttpMethod.POST, "/flight-service/v1/api/flights").hasRole(Role.ADMIN.getRole())
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }
}
