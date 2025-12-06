package Managefolio.admin.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        // Skip session check for login, logout, and static resources
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || 
            requestURI.equals("/logout") || 
            requestURI.startsWith("/css/") || 
            requestURI.startsWith("/js/") || 
            requestURI.startsWith("/images/") ||
            requestURI.startsWith("/uploads/")) {
            return true;
        }

        // Check if session exists and is valid
        if (session != null && !session.isNew()) {
            // Update last access time (this happens automatically)
            session.setAttribute("lastAccessTime", System.currentTimeMillis());
            return true;
        }

        return true; // Let Spring Security handle the authentication
    }
}