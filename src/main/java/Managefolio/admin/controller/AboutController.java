package Managefolio.admin.controller;

import Managefolio.admin.model.About;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.AboutRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/about")
@PreAuthorize("hasRole('ADMIN')")
public class AboutController {

    @Autowired
    private AboutRepository aboutRepository;

    @Autowired
    private ProfileRepository profileRepository;

    // 🌐 Default route: redirect to first profile's about
    @GetMapping
    public String redirectToFirstProfile() {
        Long firstId = profileRepository.findAll().stream()
            .findFirst()
            .map(Profile::getId)
            .orElseThrow(() -> new IllegalStateException("No profiles available"));
        return "redirect:/admin/about/" + firstId;
    }

    // 🌐 View or edit About section for a profile
    @GetMapping("/{profileId}")
    public String viewOrEditAbout(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));

        About about = aboutRepository.findByProfileId(profileId)
            .orElse(About.builder().profile(profile).build());

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin); // ✅ Dynamic role check

        model.addAttribute("about", about);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "about/form");
        return "layout/base";
    }

    // 📝 Save or update About section
    @PostMapping("/save")
    public String saveAbout(@ModelAttribute About about, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        boolean isNew = about.getId() == null;
        aboutRepository.save(about);
        redirectAttributes.addFlashAttribute("successMessage", 
            isNew ? "About section added successfully!" : "About section updated successfully!");
        return "redirect:/admin/about/" + about.getProfile().getId(); // ✅ Stay in profile context
    }
}