package com.techworld.apigateway.security.keycloak.roles;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakRoleServiceImpl implements KeyCloakRoleService {
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.clientId}")
    private String clientId;

    private final Keycloak keycloak;

    private ClientResource getClientResource() {
        ClientsResource clientsResource = keycloak.realm(realm).clients();
        List<ClientRepresentation> clients = clientsResource.findByClientId(clientId);
        if (clients.isEmpty()) {
            throw new IllegalStateException("Client not found: " + clientId);
        }
        return clientsResource.get(clients.get(0).getId());
    }

    @Override
    public void assignRole(String userId, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        ClientResource clientResource = getClientResource();
        RoleResource roleResource = clientResource.roles().get(roleName);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();

        UserResource userResource = realmResource.users().get(userId);
        userResource.roles()
                .clientLevel(clientResource.toRepresentation().getId())
                .add(Collections.singletonList(roleRepresentation));
    }

    @Override
    public RoleRegistrationRecord createRole(RoleRegistrationRecord roleRegistrationRecord) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(roleRegistrationRecord.roleName());
        roleRepresentation.setDescription(roleRegistrationRecord.description());

        ClientResource clientResource = getClientResource();
        clientResource.roles().create(roleRepresentation);
        return roleRegistrationRecord;
    }

    @Override
    public void deleteRole(String roleName) {
        ClientResource clientResource = getClientResource();
        RoleResource roleResource = clientResource.roles().get(roleName);
        roleResource.remove();
    }
}
