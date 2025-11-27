package com.smartshop.smartshop.session;

import com.smartshop.smartshop.exceptions.UnauthorizedException;
import com.smartshop.smartshop.models.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionService {

    private final HttpServletRequest request;

    public SessionUser getCurrentUser() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("Authentication required");
        }
        SessionUser user = (SessionUser) session.getAttribute(SessionConstants.CURRENT_USER);
        if (user == null) {
            throw new UnauthorizedException("Authentication required");
        }
        return user;
    }

    public void requireRole(UserRole requiredRole) {
        SessionUser user = getCurrentUser();
        if (requiredRole != null && user.getRole() != requiredRole) {
            throw new UnauthorizedException("Access denied for role " + user.getRole());
        }
    }
}

