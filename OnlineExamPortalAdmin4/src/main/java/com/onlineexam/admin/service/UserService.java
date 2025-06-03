package com.onlineexam.admin.service;

import com.onlineexam.admin.dto.UserCreateDTO;
import com.onlineexam.admin.dto.UserLoginDTO;
import com.onlineexam.admin.dto.UserResponseDTO;
import com.onlineexam.admin.dto.UserUpdateDTO;
import com.onlineexam.admin.entity.User;
import com.onlineexam.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        // IMPORTANT: In a real application, you'd encrypt the password here using BCryptPasswordEncoder
        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword()); // Hash this in a real app!
        user.setRole(userCreateDTO.getRole().toUpperCase()); // Store role in uppercase
        User savedUser = userRepository.save(user);
        return convertToUserResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToUserResponseDTO);
    }

    public Optional<UserResponseDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserResponseDTO);
    }

    public UserResponseDTO updateUser(Integer id, UserUpdateDTO userUpdateDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(userUpdateDTO.getName());
            existingUser.setEmail(userUpdateDTO.getEmail());
            // Only update password if a new one is provided (in a real app, securely)
            if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userUpdateDTO.getPassword()); // Hash this!
            }
            existingUser.setRole(userUpdateDTO.getRole().toUpperCase()); // Store role in uppercase
            User updatedUser = userRepository.save(existingUser);
            return convertToUserResponseDTO(updatedUser);
        } else {
            return null; // Or throw an exception
        }
    }

    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Authenticates user using UserLoginDTO
    public Optional<UserResponseDTO> authenticateUser(UserLoginDTO loginDetails) {
        Optional<User> userOptional = userRepository.findByEmail(loginDetails.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // In a real app, you'd compare hashed passwords here (e.g., with BCryptPasswordEncoder)
            if (user.getPassword().equals(loginDetails.getPassword())) {
                return Optional.of(convertToUserResponseDTO(user));
            }
        }
        return Optional.empty();
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