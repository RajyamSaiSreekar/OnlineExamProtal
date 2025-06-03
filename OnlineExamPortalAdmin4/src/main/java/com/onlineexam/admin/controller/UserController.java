package com.onlineexam.admin.controller;

import com.onlineexam.admin.dto.IdResponseDTO;
import com.onlineexam.admin.dto.UserCreateDTO;
import com.onlineexam.admin.dto.UserLoginDTO;
import com.onlineexam.admin.dto.UserResponseDTO;
import com.onlineexam.admin.dto.UserUpdateDTO;
import com.onlineexam.admin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<IdResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        // In a real application, you would also check for unique email before saving
        Optional<UserResponseDTO> existingUser = userService.getUserByEmail(userCreateDTO.getEmail());
        if (existingUser.isPresent()) {
            return new ResponseEntity<>(new IdResponseDTO(null, "User with this email already exists"), HttpStatus.CONFLICT);
        }
        UserResponseDTO createdUser = userService.createUser(userCreateDTO);
        return new ResponseEntity<>(new IdResponseDTO(createdUser.getUserId(), "User registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        Optional<UserResponseDTO> authenticatedUser = userService.authenticateUser(userLoginDTO);
        return authenticatedUser.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        Optional<UserResponseDTO> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}