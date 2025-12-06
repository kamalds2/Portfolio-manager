package Managefolio.admin.controller;

import Managefolio.admin.model.*;
import Managefolio.admin.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProjectRepository projectRepository;
    private final JobExperienceRepository jobExperienceRepository;
    private final EducationRepository educationRepository;
    private final SkillRepository skillRepository;
    private final AboutRepository aboutRepository;

    public DashboardController(UserRepository userRepository,
                               ProfileRepository profileRepository,
                               ProjectRepository projectRepository,
                               JobExperienceRepository jobExperienceRepository,
                               EducationRepository educationRepository,
                               SkillRepository skillRepository,
                               AboutRepository aboutRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.projectRepository = projectRepository;
        this.jobExperienceRepository = jobExperienceRepository;
        this.educationRepository = educationRepository;
        this.skillRepository = skillRepository;
        this.aboutRepository = aboutRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication,
                               @org.springframework.web.bind.annotation.RequestParam(required = false) String error) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // Handle error messages
        if ("profile_exists".equals(error)) {
            model.addAttribute("errorMessage", "You can only create one profile per user. Please edit your existing profile instead.");
        }

        if (isAdmin) {
            model.addAttribute("profiles", profileRepository.findAll());
            model.addAttribute("activeProfileCount", profileRepository.countByActiveTrue());
        } else if (user != null) {
        	model.addAttribute("user", user); // ✅ Required for Thymeleaf to access ${user.username}
            List<Profile> userProfiles = profileRepository.findByUserId(user.getId());
            Profile profile = userProfiles.isEmpty() ? null : userProfiles.get(0); // Get first profile

            if (profile == null) {
                model.addAttribute("noProfile", true); // ✅ flag for empty state
            } else {
                Long profileId = profile.getId();

                model.addAttribute("activeProfile", profile);
                model.addAttribute("userProfile", profile); // ✅ Add userProfile for template access
                model.addAttribute("skills", skillRepository.findByProfileId(profileId));
                model.addAttribute("projects", projectRepository.findByProfileId(profileId));
                model.addAttribute("educationList", educationRepository.findByProfileId(profileId));
                model.addAttribute("jobs", jobExperienceRepository.findByProfileId(profileId));
                model.addAttribute("about", aboutRepository.findByProfileId(profileId));

                model.addAttribute("profileImageUrl", profile.getProfileImage());
                model.addAttribute("userFullName", profile.getFullName());
                model.addAttribute("userEmail", profile.getEmail());
                model.addAttribute("userStatus", profile.isActive() ? "Active" : "Inactive");
            }
        }

        model.addAttribute("viewName", "dashboard");
        return "layout/base";
    }
}