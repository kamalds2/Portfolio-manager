package Managefolio.admin.controller;

import Managefolio.admin.model.Profile;
import Managefolio.admin.model.User;
import Managefolio.admin.repository.ProfileRepository;
import Managefolio.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/profile")
public class ProfileController {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    // 🌐 Admin View: List Profiles
    @GetMapping
    public String listProfiles(Model model) {
        List<Profile> profiles = profileRepository.findAll();
        model.addAttribute("profiles", profiles);
        model.addAttribute("viewName", "profile/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: View Profile with contextual header
    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + id));
        model.addAttribute("activeProfile", profile); // ✅ Enables dynamic header
        model.addAttribute("viewName", "profile/view");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show Form for New Profile
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("viewName", "profile/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show Form for Editing Profile
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + id));
        model.addAttribute("profile", profile);
        model.addAttribute("viewName", "profile/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 📝 Admin View: Save Profile (Create or Update)
    @PostMapping({"/add", "/edit/{id}"})
    public String saveProfile(@ModelAttribute Profile profile,
                              @AuthenticationPrincipal UserDetails userDetails) {
        if (profile.getUser() == null) {
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
            profile.setUser(user); // ✅ Fixes user_id null
        }
        profileRepository.save(profile);
        return "redirect:/admin/profile";
    }

    // ❌ Admin View: Delete Profile
    @GetMapping("/delete/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileRepository.deleteById(id);
        return "redirect:/admin/profile";
    }
}