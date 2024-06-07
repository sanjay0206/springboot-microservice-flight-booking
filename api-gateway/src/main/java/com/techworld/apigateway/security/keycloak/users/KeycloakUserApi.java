package com.techworld.apigateway.security.keycloak.users;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class KeycloakUserApi {

    private final KeycloakUserService userService;

    @PostMapping("/add")
    public UserRegistrationRecord createUser(@RequestBody UserRegistrationRecord userRegistrationRecord) {
        return userService.createUser(userRegistrationRecord);
    }

    @GetMapping("/{userName}")
    public List<UserRepresentation> getUser(@PathVariable String userName) {
        return userService.getUser(userName);
    }

    @DeleteMapping("/delete/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User Deleted Successfully.";
    }
}
