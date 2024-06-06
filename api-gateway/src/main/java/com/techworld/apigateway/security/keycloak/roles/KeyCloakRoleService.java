package com.techworld.apigateway.security.keycloak.roles;

public interface KeyCloakRoleService {
    void assignRole(String userId,String roleName);
    RoleRegistrationRecord createRole(RoleRegistrationRecord roleRegistrationRecord);
    void deleteRole(String roleName);
}
