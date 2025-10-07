package Managefolio.admin.controller;

import Managefolio.admin.model.Education;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.EducationRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/education")
@PreAuthorize("hasRole('ADMIN')")
public class EducationController {

    @Autowired private EducationRepository educationRepository;
    @Autowired private ProfileRepository profileRepository;

    // 🌐 Admin View: List education for a profile
    @GetMapping("/{profileId}")
    public String listEducation(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        model.addAttribute("educationList", educationRepository.findByProfileId(profileId));
        model.addAttribute("activeProfile", profile); // ✅ Enables dynamic header
        model.addAttribute("viewName", "education/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show form to create education
    @GetMapping("/new/{profileId}")
    public String createEducationForm(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        Education education = new Education();
        education.setProfile(profile);
        model.addAttribute("education", education);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "education/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 📝 Admin View: Save education (create or update)
    @PostMapping("/add")
    public String saveEducation(@ModelAttribute Education education) {
        educationRepository.save(education);
        return "redirect:/admin/education/" + education.getProfile().getId(); // ✅ Stay in profile context
    }

    // 🌐 Admin View: Show form to edit education
    @GetMapping("/edit/{id}")
    public String editEducation(@PathVariable Long id, Model model) {
        Education education = educationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid education ID: " + id));
        Profile profile = education.getProfile();
        model.addAttribute("education", education);
        model.addAttribute("activeProfile", profile); // ✅ For header context
        model.addAttribute("viewName", "education/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // ❌ Admin View: Delete education
    @GetMapping("/delete/{id}")
    public String deleteEducation(@PathVariable Long id) {
        Education education = educationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid education ID: " + id));
        Long profileId = education.getProfile().getId();
        educationRepository.deleteById(id);
        return "redirect:/admin/education/" + profileId; // ✅ Stay in profile context
    }
}