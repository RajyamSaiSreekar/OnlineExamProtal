package com.OnlineExamProtal.UserModule.Service;

import org.springframework.security.core.userdetails.User;

import com.OnlineExamProtal.UserModule.DTO.LoginDTO;
import com.OnlineExamProtal.UserModule.DTO.UserDTO;
import com.OnlineExamProtal.UserModule.DTO.UserRegistrationDTO;
import com.OnlineExamProtal.UserModule.DTO.UserRequestDTO;
import com.OnlineExamProtal.UserModule.Model.Role;

public interface UserService {
	UserRequestDTO getUserById(Long userId);
    UserDTO registerUser(UserRegistrationDTO dto);
    UserDTO loginUser(LoginDTO dto);
    UserDTO updateUser(Long userId, UserRegistrationDTO dto);
    void assignRole(Long userId, Role role); // Admin-only
}


