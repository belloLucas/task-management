package com.bellolucas.tasks_management.controllers;

import com.bellolucas.tasks_management.dto.user.CreateUserDTO;
import com.bellolucas.tasks_management.dto.user.UpdateUserDTO;
import com.bellolucas.tasks_management.dto.user.UserResponseDTO;
import com.bellolucas.tasks_management.dto.auth.AuthRequestDTO;
import com.bellolucas.tasks_management.dto.auth.AuthResponseDTO;
import com.bellolucas.tasks_management.entities.user.User;
import com.bellolucas.tasks_management.services.UserService;
import com.bellolucas.tasks_management.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid CreateUserDTO data) {
        userService.createUser(data);
        
        AuthRequestDTO authRequest = new AuthRequestDTO(data.email(), data.password());
        AuthResponseDTO authResponse = authenticationService.authenticate(authRequest);
        
        return ResponseEntity.status(201).body(authResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO data) {
        User updatedUser = userService.updateUser(id, data);
        return ResponseEntity.ok(new UserResponseDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
