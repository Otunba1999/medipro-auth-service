package com.otunba.medipro.controller;

import com.otunba.medipro.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/medipro")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "use for managing user")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "retrieve all user from the system only admin authorize", method = "Post" )
    public ResponseEntity<?> getUsers() {
        var response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
//    @PreAuthorize("hasAnyAuthority('user:read')")
    public ResponseEntity<?> getUser() {
        var response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
