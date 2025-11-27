package com.smartshop.smartshop.controllers;

import com.smartshop.smartshop.dto.auth.LoginRequest;
import com.smartshop.smartshop.dto.auth.LoginResponse;
import com.smartshop.smartshop.dto.user.UserResponse;
import com.smartshop.smartshop.exceptions.UnauthorizedException;
import com.smartshop.smartshop.services.AuthService;
import com.smartshop.smartshop.session.SessionConstants;
import com.smartshop.smartshop.session.SessionUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UserResponse user = authService.authenticate(request);
        SessionUser sessionUser = new SessionUser(user.getId(), user.getUsername(), user.getRole());
        session.setAttribute(SessionConstants.CURRENT_USER, sessionUser);
        return ResponseEntity.ok(new LoginResponse("Login successful", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUser> currentUser(HttpSession session) {
        SessionUser sessionUser = (SessionUser) session.getAttribute(SessionConstants.CURRENT_USER);
        if (sessionUser == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        return ResponseEntity.ok(sessionUser);
    }
}

