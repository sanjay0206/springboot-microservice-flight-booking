package com.techworld.apigateway.security.keycloak.service;

import com.techworld.apigateway.security.keycloak.model.RoleRegistrationRecord;

public interface KeyCloakRoleService {

    void assignRole(String userId,String roleName);

    RoleRegistrationRecord createRole(RoleRegistrationRecord roleRegistrationRecord);

    void deleteRole(String roleName);
}
