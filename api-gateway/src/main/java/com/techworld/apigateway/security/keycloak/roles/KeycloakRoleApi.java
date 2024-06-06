package com.techworld.apigateway.security.keycloak.roles;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/roles")
@RequiredArgsConstructor
public class KeycloakRoleApi {
    private final KeyCloakRoleService roleService;

    @PutMapping("/assign-role/{userId}")
    public String assignRole(@PathVariable String userId, @RequestParam String roleName){
        roleService.assignRole(userId, roleName);
        return "Role Assigned Successfully.";
    }

    @PostMapping("/add")
    public RoleRegistrationRecord createRole(@RequestBody RoleRegistrationRecord roleRegistrationRecord) {
       return roleService.createRole(roleRegistrationRecord);
    }

    @DeleteMapping("/delete")
    public String deleteRole(@RequestParam String roleName) {
        roleService.deleteRole(roleName);
        return "Role Deleted Successfully.";
    }
}
