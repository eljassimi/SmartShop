package com.smartshop.smartshop.security;

import com.smartshop.smartshop.exceptions.UnauthorizedException;
import com.smartshop.smartshop.session.SessionConstants;
import com.smartshop.smartshop.session.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("Authentication required");
        }
        SessionUser sessionUser = (SessionUser) session.getAttribute(SessionConstants.CURRENT_USER);
        if (sessionUser == null) {
            throw new UnauthorizedException("Authentication required");
        }
        request.setAttribute(SessionConstants.CURRENT_USER, sessionUser);
        return true;
    }
}


