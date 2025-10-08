package Managefolio.admin.controller;

import Managefolio.admin.model.Projects;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.ProjectRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/admin/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProfileRepository profileRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public String redirectToFirstProfile() {
        Long firstId = profileRepository.findAll().stream()
            .findFirst()
            .map(Profile::getId)
            .orElseThrow(() -> new IllegalStateException("No profiles available"));
        return "redirect:/admin/projects/" + firstId;
    }

    @GetMapping("/{profileId}")
    public String listProjects(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        List<Projects> projects = projectRepository.findByProfileId(profileId);

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("projects", projects);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "projects/list");
        return "layout/base";
    }

    @GetMapping("/view/{id}")
    public String viewProject(@PathVariable Long id, Model model, Authentication authentication) {
        Projects project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        Profile profile = project.getProfile();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("project", project);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "projects/view");
        return "layout/base";
    }

    @GetMapping("/new/{profileId}")
    public String showCreateForm(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        Projects project = new Projects();
        project.setProfile(profile);

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("project", project);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "projects/form");
        return "layout/base";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Projects project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        Profile profile = project.getProfile();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("project", project);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "projects/form");
        return "layout/base";
    }

    @PostMapping({"/add", "/edit/{id}"})
    public String saveProject(@ModelAttribute Projects project) {
        projectRepository.save(project);
        return "redirect:/admin/projects/" + project.getProfile().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        Projects project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        Long profileId = project.getProfile().getId();

        projectRepository.deleteById(id);
        return "redirect:/admin/projects/" + profileId;
    }
}