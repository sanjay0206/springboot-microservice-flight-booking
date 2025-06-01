package com.techworld.apigateway.security.keycloak.service;

import com.techworld.apigateway.security.keycloak.model.UserRegistrationRecord;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakUserService {

    UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord);

    List<UserRepresentation> getUser(String userName);

    void deleteUser(String userId);

    UserResource getUserResource(String userId);
}
