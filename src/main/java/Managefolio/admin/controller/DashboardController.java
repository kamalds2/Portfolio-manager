package Managefolio.admin.controller;

import Managefolio.admin.model.User;
import Managefolio.admin.model.Projects;
import Managefolio.admin.model.JobExperience;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.UserRepository;
import Managefolio.admin.repository.ProjectRepository;
import Managefolio.admin.repository.JobExperienceRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final JobExperienceRepository jobExperienceRepository;
    private final ProfileRepository profileRepository;

    public DashboardController(UserRepository userRepository,
                               ProjectRepository projectRepository,
                               JobExperienceRepository jobExperienceRepository,
                               ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.jobExperienceRepository = jobExperienceRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            model.addAttribute("profiles", profileRepository.findAll());
            model.addAttribute("activeProfileCount", profileRepository.countByActiveTrue());
        } else if (user != null) {
            Profile profile = profileRepository.findByUserId(user.getId());

            if (profile != null) {
                Long profileId = profile.getId();

                List<Projects> activeProjects = projectRepository.findByProfileIdAndStatus(profileId, "ACTIVE");
                JobExperience currentJob = jobExperienceRepository.findTopByProfileIdAndEndDateIsNull(profileId);

                model.addAttribute("profileImageUrl", profile.getProfileImage());
                model.addAttribute("userFullName", profile.getFullName());
                model.addAttribute("userEmail", profile.getEmail());
                model.addAttribute("userStatus", profile.isActive() ? "Active" : "Inactive");
                model.addAttribute("activeProjectsCount", activeProjects.size());
                model.addAttribute("currentJob", currentJob);
                model.addAttribute("user", user);
            }
        }

        model.addAttribute("viewName", "dashboard");
        return "layout/base";
    }
}