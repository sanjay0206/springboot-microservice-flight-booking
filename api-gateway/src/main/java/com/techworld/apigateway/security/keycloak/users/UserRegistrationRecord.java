package com.techworld.apigateway.security.keycloak.users;

public record UserRegistrationRecord(String username, String email, String firstName, String lastName, String password)
{
}
