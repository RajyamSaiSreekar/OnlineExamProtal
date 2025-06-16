package com.onlineexam.admin.dto;

import com.onlineexam.admin.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Integer userId;
    private String name;
    private String email;
    private Role role;
    // Password is intentionally omitted for security
}