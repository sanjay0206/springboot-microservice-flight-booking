package com.techworld.apigateway.security.keycloak.roles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("admin"), USER("user");

    private final String role;
}