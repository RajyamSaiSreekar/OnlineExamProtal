package com.OnlineExamProtal.UserModule.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.OnlineExamProtal.UserModule.DTO.LoginDTO;
import com.OnlineExamProtal.UserModule.DTO.UserDTO;
import com.OnlineExamProtal.UserModule.DTO.UserRegistrationDTO;
import com.OnlineExamProtal.UserModule.DTO.UserRequestDTO;
import com.OnlineExamProtal.UserModule.Exception.CustomException;
import com.OnlineExamProtal.UserModule.Model.Role;
import com.OnlineExamProtal.UserModule.Model.User;
import com.OnlineExamProtal.UserModule.Repository.UserRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private final UserRepository userRepository;
	@Autowired
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserRequestDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
        return requestToDTO(user);
    }

       
    @Override
    public UserDTO registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }
        

    String password = dto.getPassword();
    String confirmPassword = dto.getConfirmPassword();

    if (password == null || confirmPassword == null || !password.equals(confirmPassword)) 
    {
    	throw new CustomException("Password and confirm password do not match");
    }


        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
               
        if(userRepository.count() == 0) {
        	user.setRole(Role.ADMIN);
        }else {
        user.setRole(Role.STUDENT); 
        }

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO loginUser(LoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials");
        }

        return mapToDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public void assignRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
    private UserRequestDTO requestToDTO(User user) {
    	UserRequestDTO dto = new UserRequestDTO();
    	dto.setUserId(user.getUserId());
    	dto.setName(user.getName());
    	dto.setEmail(user.getEmail());
    	dto.setPassword(user.getPassword());
    	dto.setRole(user.getRole());
    	return dto;
    }
}


