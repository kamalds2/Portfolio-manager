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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

import Managefolio.admin.services.UploadService;

@Controller
@RequestMapping("/admin/profile")
public class ProfileController {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UploadService uploadService;

    @Autowired
    public ProfileController(ProfileRepository profileRepository, UserRepository userRepository, UploadService uploadService) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.uploadService = uploadService;
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
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                 @org.springframework.web.bind.annotation.RequestParam(required = false) String error) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Profile> existingProfiles = profileRepository.findByUserId(user.getId());
        if (!existingProfiles.isEmpty()) {
            model.addAttribute("errorMessage", "You can only create one profile per user. Please edit your existing profile instead.");
            model.addAttribute("viewName", "dashboard");
            return "redirect:/dashboard?error=profile_exists";
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
                              @AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam(name = "resumeFile", required = false) MultipartFile resumeFile,
                              org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (profile.getUser() == null) {
            profile.setUser(user);
        }

        // ✅ Enforce one profile per user (only for new profiles)
        if (profile.getId() == null) {
            // Creating new profile - check if user already has one
            List<Profile> existing = profileRepository.findByUserId(profile.getUser().getId());
            if (!existing.isEmpty()) {
                throw new IllegalStateException("User already has a profile");
            }
        }

        // ✅ Set audit fields
        if (profile.getId() == null) {
            profile.setCreatedBy(user.getId());
        }
        profile.setUpdatedBy(user.getId());

        // Save profile first to get the ID for file uploads
        Profile savedProfile = profileRepository.save(profile);

        // Handle uploaded files (if present) - use the saved profile's ID
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = uploadService.storeProfileImage(savedProfile.getId(), imageFile);
                savedProfile.setProfileImage(imagePath);
                profileRepository.save(savedProfile); // Update with image path
            }
            if (resumeFile != null && !resumeFile.isEmpty()) {
                String resumePath = uploadService.storeResume(savedProfile.getId(), resumeFile);
                savedProfile.setResumeUrl(resumePath);
                profileRepository.save(savedProfile); // Update with resume path
            }
        } catch (Exception e) {
            // If upload failed, log error and continue (profile saved without files)
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Profile saved but file upload failed: " + e.getMessage());
            return "redirect:/dashboard";
        }
        
        boolean isNewProfile = profile.getId() == null;
        redirectAttributes.addFlashAttribute("successMessage", 
            isNewProfile ? "Profile added successfully!" : "Profile updated successfully!");
        return "redirect:/dashboard";
    }

    // ❌ Admin View: Delete profile
    @GetMapping("/delete/{id}")
    public String deleteProfile(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            profileRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Profile deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete profile: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
}