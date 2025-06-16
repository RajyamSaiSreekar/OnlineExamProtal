package com.onlineexam.admin.service;

import com.onlineexam.admin.dto.UserCreateDTO;
import com.onlineexam.admin.dto.UserLoginDTO;
import com.onlineexam.admin.dto.UserResponseDTO;
import com.onlineexam.admin.dto.UserUpdateDTO;
import com.onlineexam.admin.entity.Role;
import com.onlineexam.admin.entity.User;
import com.onlineexam.admin.repository.UserRepository;
import com.onlineexam.admin.Exception.CustomException; // Import your CustomException
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
       
        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());
        if(userRepository.count()==0) {
            user.setRole(Role.ADMIN);
        }else {
            user.setRole(Role.STUDENT);
        }
        User savedUser = userRepository.save(user);
        return convertToUserResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Integer id) throws CustomException { // Added throws CustomException
        return userRepository.findById(id)
                .map(this::convertToUserResponseDTO)
                .orElseThrow(() -> new CustomException("User with ID " + id + " not found")); // Throws CustomException if not found
    }

    public UserResponseDTO getUserByEmail(String email) throws CustomException { // Added throws CustomException
        return userRepository.findByEmail(email)
                .map(this::convertToUserResponseDTO)
                .orElseThrow(() -> new CustomException("User with email " + email + " not found")); // Throws CustomException if not found
    }

    public UserResponseDTO updateUser(Integer id, UserUpdateDTO userUpdateDTO) throws CustomException { // Added throws CustomException
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User with ID " + id + " not found")); // Throws CustomException if not found

        existingUser.setName(userUpdateDTO.getName());
        existingUser.setEmail(userUpdateDTO.getEmail());
        // Only update password if a new one is provided 
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userUpdateDTO.getPassword());
            
        }
       
        
        User updatedUser = userRepository.save(existingUser);
        return convertToUserResponseDTO(updatedUser);
    }

    public void assignRole(Integer userId, Role role) throws CustomException { // Added throws CustomException
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException("User with ID " + userId + " not found")); // Changed RuntimeException to CustomException
        user.setRole(role);
        userRepository.save(user);
    }

    public boolean deleteUser(Integer id) throws CustomException { // Added throws CustomException
        if (!userRepository.existsById(id)) {
            throw new CustomException("User with ID " + id + " not found"); // Throws CustomException if not found
        }
        userRepository.deleteById(id);
        return true;
    }

    // Authenticates user using UserLoginDTO
    public UserResponseDTO authenticateUser(UserLoginDTO loginDetails) throws CustomException { // Added throws CustomException
        User user = userRepository.findByEmail(loginDetails.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials: User not found")); // Throws CustomException if not found

       
        if (user.getPassword().equals(loginDetails.getPassword())) {
            return convertToUserResponseDTO(user);
        } else {
            throw new CustomException("Invalid credentials: Incorrect password"); // Throws CustomException for incorrect password
        }
    }

    // Helper method to convert Entity to DTO (omits sensitive data like password)
    private UserResponseDTO convertToUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}