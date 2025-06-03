package com.OnlineExamProtal.UserModule.DTO;

import com.OnlineExamProtal.UserModule.Model.Role; // Import Role enum

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO {
	    private Long userId;
	    private String name;
	    private String email;
	    private Role role; // Include role
	    private String token; // To hold the JWT token after login
}