package com.OnlineExamProtal.UserModule.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import PreAuthorize
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
@RequestMapping("/examProtal/userModule")
public class UserController {
	private final UserService userService;
	// Constructor-based dependency injection for UserService
	@Autowired
	public UserController(UserService userService) {
		this.userService=userService;
	}
	// EndPoint to get all Users
	// Only ADMIN can view all users
	@GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserRequestDTO>> findAllUsers() {
		return ResponseEntity.ok(userService.findAll());
	}

	// EndPoint to get user by id
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')") // Both ADMIN and STUDENT can view a user by ID
    public ResponseEntity<UserRequestDTO> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	// EndPoint to register new User
	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerNewUser(@Valid @RequestBody UserRegistrationDTO dto) {
		return ResponseEntity.ok(userService.registerUser(dto));
	}
	// EndPoint to login the existing user
	@PostMapping("/login")
	public ResponseEntity<UserDTO> loginUser(@RequestBody @Valid LoginDTO dto) {
		return ResponseEntity.ok(userService.loginUser(dto));
	}
	
	//EndPoint to update the user profile
	@PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserRegistrationDTO dto) {
		return ResponseEntity.ok(userService.updateUser(id, dto));
	}
	//EndPoint to update the role of a user
	// Only ADMIN can assign roles
	@PutMapping("/{id}/role")
	@PreAuthorize("hasRole('ADMIN')") 
	public ResponseEntity<Void> assignRoleToUser(@PathVariable Long id, @RequestParam Role role) {
		userService.assignRole(id, role);
		return ResponseEntity.ok().build();
	}
}