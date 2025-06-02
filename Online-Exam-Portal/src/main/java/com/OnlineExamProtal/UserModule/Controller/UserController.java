package com.OnlineExamProtal.UserModule.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.OnlineExamProtal.UserModule.DTO.LoginDTO;
import com.OnlineExamProtal.UserModule.DTO.UserDTO;
import com.OnlineExamProtal.UserModule.DTO.UserRegistrationDTO;
import com.OnlineExamProtal.UserModule.DTO.UserRequestDTO;
import com.OnlineExamProtal.UserModule.Model.Role;
import com.OnlineExamProtal.UserModule.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequestDTO> get(@PathVariable Long id){
    	return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegistrationDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid LoginDTO dto) {
        return ResponseEntity.ok(userService.loginUser(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserRegistrationDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @RequestParam Role role) {
        userService.assignRole(id, role);
        return ResponseEntity.ok().build();
    }
}

