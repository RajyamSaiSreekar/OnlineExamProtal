package com.onlineexam.admin.service;

import com.onlineexam.admin.dto.UserCreateDTO;
import com.onlineexam.admin.dto.UserLoginDTO;
import com.onlineexam.admin.dto.UserResponseDTO;
import com.onlineexam.admin.dto.UserUpdateDTO;
import com.onlineexam.admin.entity.User;
import com.onlineexam.admin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserResponseDTO userResponseDTO;
    private UserLoginDTO userLoginDTO;

    @BeforeEach
    void setUp() {
        user = new User(1, "John Doe", "john.doe@example.com", "password123", "ADMIN");
        userCreateDTO = new UserCreateDTO("John Doe", "john.doe@example.com", "password123", "ADMIN");
        userResponseDTO = new UserResponseDTO(1, "John Doe", "john.doe@example.com", "ADMIN");
        userLoginDTO = new UserLoginDTO("john.doe@example.com", "password123");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.createUser(userCreateDTO);

        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserResponseDTO> results = userService.getAllUsers();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(user.getName(), results.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByIdFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(user.getUserId(), result.get().getUserId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserById(1);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByEmailFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void testUpdateUserFound() {
        User updatedUser = new User(1, "Jane Doe", "jane.doe@example.com", "newpassword", "STUDENT");
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Jane Doe", "jane.doe@example.com", "newpassword", "STUDENT");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDTO result = userService.updateUser(1, userUpdateDTO);

        assertNotNull(result);
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getRole(), result.getRole());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Jane Doe", "jane.doe@example.com", "newpassword", "STUDENT");
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserResponseDTO result = userService.updateUser(1, userUpdateDTO);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUserExists() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotExists() {
        when(userRepository.existsById(1)).thenReturn(false);

        boolean result = userService.deleteUser(1);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(0)).deleteById(anyInt());
    }

    @Test
    void testAuthenticateUserSuccess() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.authenticateUser(userLoginDTO);

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testAuthenticateUserInvalidPassword() {
        UserLoginDTO wrongPasswordLogin = new UserLoginDTO("john.doe@example.com", "wrongpassword");
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.authenticateUser(wrongPasswordLogin);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testAuthenticateUserEmailNotFound() {
        UserLoginDTO nonExistentEmailLogin = new UserLoginDTO("nonexistent@example.com", "password123");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.authenticateUser(nonExistentEmailLogin);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}