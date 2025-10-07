package Managefolio.admin.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController
public class PasswordDebugController {

    private final PasswordEncoder passwordEncoder;

    public PasswordDebugController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void generateHash() {
        String rawPassword = "admin123";
        String encoded = passwordEncoder.encode(rawPassword);
        System.out.println("🔐 Encoded password for '" + rawPassword + "':");
        System.out.println(encoded);
    }

    @GetMapping("/test-password")
    public void testPassword(@RequestParam String raw, @RequestParam String encoded) {
        boolean match = passwordEncoder.matches(raw, encoded);
        System.out.println("✅ Match: " + match);
    }
}