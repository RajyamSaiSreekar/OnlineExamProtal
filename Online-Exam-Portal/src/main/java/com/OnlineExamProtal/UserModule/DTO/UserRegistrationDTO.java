package com.OnlineExamProtal.UserModule.DTO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}

