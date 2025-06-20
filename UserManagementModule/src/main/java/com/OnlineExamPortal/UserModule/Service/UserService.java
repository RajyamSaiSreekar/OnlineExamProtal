package com.OnlineExamPortal.UserModule.Service;

import com.OnlineExamPortal.UserModule.DTO.LoginDTO;
import com.OnlineExamPortal.UserModule.DTO.ServiceTokenRequestDTO;
import com.OnlineExamPortal.UserModule.DTO.UserDTO;
import com.OnlineExamPortal.UserModule.DTO.UserRegistrationDTO;
import com.OnlineExamPortal.UserModule.DTO.UserRequestDTO;
import com.OnlineExamPortal.UserModule.Model.Role;

import java.util.List;

public interface UserService {
    List<UserRequestDTO> findAllUsers();
    UserRequestDTO getUserById(Long userId);
    UserDTO registerUser(UserRegistrationDTO dto);
    UserDTO loginUser(LoginDTO dto);
    UserDTO updateUser(Long userId, UserRegistrationDTO dto);
    void assignRole(Long userId, Role role);
    UserDTO generateServiceToken(ServiceTokenRequestDTO requestDTO);
}