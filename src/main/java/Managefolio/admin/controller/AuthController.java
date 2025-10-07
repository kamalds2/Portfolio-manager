package Managefolio.admin.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import Managefolio.admin.model.User;
import Managefolio.admin.services.UserService;

@Controller
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    // 🌐 Login Page
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User()); // optional for form binding
        return "login"; // no layout/base
    }

    // 🌐 Forgot Password Page
    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        return "forgot-password"; // no layout/base
    }

    // 📝 Handle Password Reset
    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                Model model) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(user);
            model.addAttribute("resetSuccess", true);
        } else {
            model.addAttribute("error", "User not found");
        }
        return "forgot-password"; // stay on same page with feedback
    }
}