package com.techworld.apigateway.security.keycloak.users;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakUserService {

    UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord);
    List<UserRepresentation> getUser(String userName);
    void deleteUser(String userId);
    UserResource getUserResource(String userId);
}
