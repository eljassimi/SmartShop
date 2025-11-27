package com.smartshop.smartshop.dto.auth;

import com.smartshop.smartshop.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private UserResponse user;
}

