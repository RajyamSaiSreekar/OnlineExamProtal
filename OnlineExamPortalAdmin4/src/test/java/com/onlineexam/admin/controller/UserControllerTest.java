package com.onlineexam.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineexam.admin.dto.UserCreateDTO;
import com.onlineexam.admin.dto.UserLoginDTO;
import com.onlineexam.admin.dto.UserResponseDTO;
import com.onlineexam.admin.dto.UserUpdateDTO;
import com.onlineexam.admin.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;
    private UserLoginDTO userLoginDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userCreateDTO = new UserCreateDTO("Test User", "test@example.com", "password123", "STUDENT");
        userUpdateDTO = new UserUpdateDTO("Updated User", "updated@example.com", "newpassword", "ADMIN");
        userLoginDTO = new UserLoginDTO("test@example.com", "password123");
        userResponseDTO = new UserResponseDTO(1, "Test User", "test@example.com", "STUDENT");
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("User created successfully"));
        verify(userService, times(1)).createUser(any(UserCreateDTO.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userResponseDTO));

        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test User"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserByIdFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.of(userResponseDTO));

        mockMvc.perform(get("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testUpdateUserFound() throws Exception {
        UserResponseDTO updatedUserResponseDTO = new UserResponseDTO(1, "Updated User", "updated@example.com", "ADMIN");
        when(userService.updateUser(eq(1), any(UserUpdateDTO.class))).thenReturn(updatedUserResponseDTO);

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
        verify(userService, times(1)).updateUser(eq(1), any(UserUpdateDTO.class));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        when(userService.updateUser(eq(1), any(UserUpdateDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).updateUser(eq(1), any(UserUpdateDTO.class));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        when(userService.deleteUser(1)).thenReturn(true);

        mockMvc.perform(delete("/api/admin/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        when(userService.deleteUser(1)).thenReturn(false);

        mockMvc.perform(delete("/api/admin/users/1"))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void testLoginUserSuccess() throws Exception {
        when(userService.authenticateUser(any(UserLoginDTO.class))).thenReturn(Optional.of(userResponseDTO));

        mockMvc.perform(post("/api/admin/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login Successful! Welcome, Test User (STUDENT)"));
        verify(userService, times(1)).authenticateUser(any(UserLoginDTO.class));
    }

    @Test
    void testLoginUserInvalidCredentials() throws Exception {
        when(userService.authenticateUser(any(UserLoginDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/admin/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid Credentials"));
        verify(userService, times(1)).authenticateUser(any(UserLoginDTO.class));
    }
}