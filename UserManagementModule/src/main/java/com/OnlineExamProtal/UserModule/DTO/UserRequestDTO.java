package com.OnlineExamProtal.UserModule.DTO;

import com.OnlineExamProtal.UserModule.Model.Role;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequestDTO {
	private Long userId;
    private String name;
    private String email;
    private Role role;

}

