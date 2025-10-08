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

    // 🌐 Admin View: List all profiles
    @GetMapping
    public String listProfiles(Model model) {
        List<Profile> profiles = profileRepository.findAll();
        model.addAttribute("profiles", profiles);
        model.addAttribute("viewName", "profile/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: View profile with contextual header
    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + id));
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "profile/view");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 User View: Show form for new profile (only if user has none)
    @GetMapping("/new")
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile existingProfile = profileRepository.findByUserId(user.getId());
        if (existingProfile != null) {
            return "redirect:/dashboard"; // ✅ Prevent multiple profiles
        }

        Profile profile = new Profile();
        profile.setUser(user);
        model.addAttribute("profile", profile);
        model.addAttribute("viewName", "profile/form");
        model.addAttribute("isAdmin", false);
        return "layout/base";
    }

    // 🌐 Admin/User View: Show form for editing profile
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + id));

        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isAdmin = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Optional: restrict non-admins from editing other profiles
        if (!isAdmin && !profile.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: You can only edit your own profile.");
        }

        model.addAttribute("profile", profile);
        model.addAttribute("viewName", "profile/form");
        model.addAttribute("isAdmin", isAdmin); // ✅ dynamic role detection
        return "layout/base";
    }

    // 📝 Save profile (create or update)
    @PostMapping({"/add", "/edit/{id}"})
    public String saveProfile(@ModelAttribute Profile profile,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (profile.getUser() == null) {
            profile.setUser(user);
        }

        // ✅ Enforce one profile per user
        Profile existing = profileRepository.findByUserId(profile.getUser().getId());
        if (existing != null && (profile.getId() == null || !existing.getId().equals(profile.getId()))) {
            throw new IllegalStateException("User already has a profile");
        }

        // ✅ Set audit fields
        if (profile.getId() == null) {
            profile.setCreatedBy(user.getId());
        }
        profile.setUpdatedBy(user.getId());

        profileRepository.save(profile);
        return "redirect:/dashboard";
    }

    // ❌ Admin View: Delete profile
    @GetMapping("/delete/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileRepository.deleteById(id);
        return "redirect:/admin/profile";
    }
}