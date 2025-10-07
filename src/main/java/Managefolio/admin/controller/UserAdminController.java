package Managefolio.admin.controller;

import Managefolio.admin.model.User;
import Managefolio.admin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("viewName", "user/list");
        model.addAttribute("isAdmin", true); // ✅ ensures admin-only links render
        return "layout/base";
    }

    @GetMapping("/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("viewName", "user/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    @PostMapping("/add")
    public String saveUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ ensure password is encoded
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("viewName", "user/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/change-password/{id}")
    public String changePasswordForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        model.addAttribute("viewName", "user/change-password");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam Long userId,
                                 @RequestParam String newPassword) {
        User user = userService.findById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        return "redirect:/admin/users";
    }
}