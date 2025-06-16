package com.onlineexam.admin.controller;

import com.onlineexam.admin.dto.IdResponseDTO;
import com.onlineexam.admin.dto.UserCreateDTO;
import com.onlineexam.admin.dto.UserLoginDTO;
import com.onlineexam.admin.dto.UserResponseDTO;
import com.onlineexam.admin.dto.UserUpdateDTO;
import com.onlineexam.admin.entity.Role;
import com.onlineexam.admin.service.UserService;
import com.onlineexam.admin.Exception.CustomException; // Import CustomException
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<IdResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) throws CustomException { // Added throws CustomException

        try {
            userService.getUserByEmail(userCreateDTO.getEmail());
            return new ResponseEntity<>(new IdResponseDTO(null, "User with this email already exists"), HttpStatus.CONFLICT);
        } catch (CustomException e) {
            // User not found, proceed with creation
            UserResponseDTO createdUser = userService.createUser(userCreateDTO);
            return new ResponseEntity<>(new IdResponseDTO(createdUser.getUserId(), "User registered successfully"), HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) throws CustomException { // Added throws CustomException
        UserResponseDTO authenticatedUser = userService.authenticateUser(userLoginDTO); // Call service directly
        return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) throws CustomException { // Added throws CustomException
        UserResponseDTO user = userService.getUserById(id); // Call service directly
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) throws CustomException { // Added throws CustomException
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO); // Call service directly
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //Only ADMIN can assign roles
    @PutMapping("{id}/role")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Integer id, @RequestParam Role role) throws CustomException { // Added throws CustomException
        userService.assignRole(id, role);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) throws CustomException { // Added throws CustomException
        userService.deleteUser(id); // Call service directly
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}