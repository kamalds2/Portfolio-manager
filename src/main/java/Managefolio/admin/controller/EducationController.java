package Managefolio.admin.controller;

import Managefolio.admin.model.Education;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.EducationRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/education")
public class EducationController {

    @Autowired private EducationRepository educationRepository;
    @Autowired private ProfileRepository profileRepository;

    // 🌐 View: List education for a profile
    @GetMapping("/{profileId}")
    public String listEducation(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("educationList", educationRepository.findByProfileId(profileId));
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "education/list");
        return "layout/base";
    }

    // 🌐 View: Show form to create education
    @GetMapping("/new/{profileId}")
    public String createEducationForm(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        Education education = new Education();
        education.setProfile(profile);

        model.addAttribute("education", education);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "education/form");
        return "layout/base";
    }

    // 📝 Save education (create or update)
    @PostMapping("/add")
    public String saveEducation(@ModelAttribute Education education, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        boolean isNew = education.getId() == null;
        educationRepository.save(education);
        redirectAttributes.addFlashAttribute("successMessage", 
            isNew ? "Education added successfully!" : "Education updated successfully!");
        return "redirect:/admin/education/" + education.getProfile().getId();
    }

    // 🌐 View: Show form to edit education
    @GetMapping("/edit/{id}")
    public String editEducation(@PathVariable Long id, Model model, Authentication authentication) {
        Education education = educationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid education ID: " + id));

        Profile profile = education.getProfile();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("education", education);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "education/form");
        return "layout/base";
    }

    // ❌ Delete education
    @GetMapping("/delete/{id}")
    public String deleteEducation(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Education education = educationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid education ID: " + id));

        Long profileId = education.getProfile().getId();
        educationRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Education deleted successfully!");
        return "redirect:/admin/education/" + profileId;
    }
}