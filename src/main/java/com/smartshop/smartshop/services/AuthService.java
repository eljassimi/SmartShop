package com.smartshop.smartshop.services;

import com.smartshop.smartshop.dto.auth.LoginRequest;
import com.smartshop.smartshop.dto.user.UserResponse;
import com.smartshop.smartshop.exceptions.BusinessException;
import com.smartshop.smartshop.mappers.UserMapper;
import com.smartshop.smartshop.models.user.User;
import com.smartshop.smartshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }
        return userMapper.toResponse(user);
    }
}

