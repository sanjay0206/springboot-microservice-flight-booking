package com.techworld.apigateway.security.keycloak.users;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class KeycloakUserServiceImpl implements KeycloakUserService {

    @Value("${keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;

    private UsersResource getUsersResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    public UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userRegistrationRecord.password());
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(user);
        log.info("response: " + response.getStatus());

        return Objects.equals(HttpStatus.CREATED.value(), response.getStatus()) ? userRegistrationRecord : null;
    }

    @Override
    public List<UserRepresentation> getUser(String userName) {
        return getUsersResource().search(userName, true);
    }

    @Override
    public void deleteUser(String userId) {
        getUsersResource().delete(userId);
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
