package Managefolio.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api")
public class SessionController {

    @GetMapping("/keepalive")
    public ResponseEntity<String> keepAlive(HttpSession session) {
        if (session != null && !session.isNew()) {
            // Touch the session to extend its lifetime
            session.setAttribute("lastKeepAlive", System.currentTimeMillis());
            return ResponseEntity.ok("Session refreshed");
        }
        return ResponseEntity.status(401).body("No active session");
    }

    @PostMapping("/clear-flash")
    public ResponseEntity<String> clearFlashMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Clear the Spring Flash Map Manager's flash maps
            Object flashMapsObj = session.getAttribute("org.springframework.web.servlet.support.SessionFlashMapManager.FLASH_MAPS");
            if (flashMapsObj instanceof java.util.List) {
                ((java.util.List<?>) flashMapsObj).clear();
            }
            
            // Remove the flash maps attribute entirely
            session.removeAttribute("org.springframework.web.servlet.support.SessionFlashMapManager.FLASH_MAPS");
            
            // Also clear any direct session attributes (in case they were set)
            session.removeAttribute("successMessage");
            session.removeAttribute("errorMessage");
            
            System.out.println("✅ Flash messages cleared from session: " + session.getId());
        }
        
        return ResponseEntity.ok("Flash messages cleared");
    }
}