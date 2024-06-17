package com.techworld.apigateway.security;

import com.techworld.apigateway.security.keycloak.roles.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Emulate SessionCreationPolicy.STATELESS

                .authorizeExchange()
                .matchers(this::internalCall).permitAll() // Skip authentication for internal API Calls

                    // Public endpoints
                    .pathMatchers("/auth/users/**").permitAll()
                    .pathMatchers("/auth/roles/**").permitAll()

                    // User role endpoints
                    .pathMatchers("/flight-search-service/v1/api/search/flights").hasRole(Role.USER.getRole())
                    .pathMatchers(HttpMethod.GET, "/flight-service/v1/api/flights/**").hasRole(Role.USER.getRole())
                    .pathMatchers(HttpMethod.PUT, "/flight-service/v1/api/flights/reserveSeats/**").hasRole(Role.USER.getRole())
                    .pathMatchers(HttpMethod.POST, "/booking-service/v1/api/bookings/**").hasRole(Role.USER.getRole())

                    // Admin role endpoints
                    .pathMatchers(HttpMethod.POST, "/flight-service/v1/api/flights").hasRole(Role.ADMIN.getRole())

                // Any other request must be authenticated
                .anyExchange().authenticated()
                .and()
                    .oauth2ResourceServer()
                    .jwt()
                    .jwtAuthenticationConverter(jwtAuthConverter);

        return http.build();
    }

    private Mono<ServerWebExchangeMatcher.MatchResult> internalCall(ServerWebExchange exchange) {
        logger.info("Inside internalCall");

        exchange.getRequest().getHeaders()
                .forEach((key, value) -> logger.info(key + "= " + value));

        // Check if the X-Internal-Request header has the value "Internal"
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (headers.containsKey("X-Internal-Request")) {
            if ("Internal".equalsIgnoreCase(headers.getFirst("X-Internal-Request"))) {
                return ServerWebExchangeMatcher.MatchResult.match();
            }
        }

        // Default to not match if the header is not present or does not have the expected value
        return ServerWebExchangeMatcher.MatchResult.notMatch();
    }
}
