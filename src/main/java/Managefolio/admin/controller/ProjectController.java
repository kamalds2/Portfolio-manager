package Managefolio.admin.controller;

import Managefolio.admin.model.Projects;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.ProjectRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/projects")
@PreAuthorize("hasRole('ADMIN')")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProfileRepository profileRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    // 🌐 Admin View: List projects for a specific profile
    @GetMapping("/{profileId}")
    public String listProjects(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        List<Projects> projects = projectRepository.findByProfileId(profileId);

        model.addAttribute("projects", projects);
        model.addAttribute("activeProfile", profile); // ✅ Enables dynamic header
        model.addAttribute("viewName", "projects/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show form for new project under a profile
    @GetMapping("/new/{profileId}")
    public String showCreateForm(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        Projects project = new Projects();
        project.setProfile(profile); // ✅ Bind profile to new project

        model.addAttribute("project", project);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "projects/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show form for editing project
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Projects project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        Profile profile = project.getProfile();

        model.addAttribute("project", project);
        model.addAttribute("activeProfile", profile); // ✅ For header context
        model.addAttribute("viewName", "projects/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 📝 Admin View: Save project (create or update)
    @PostMapping({"/add", "/edit/{id}"})
    public String saveProject(@ModelAttribute Projects project) {
        projectRepository.save(project);
        return "redirect:/admin/projects/" + project.getProfile().getId(); // ✅ Redirect to profile-scoped list
    }

    // ❌ Admin View: Delete project
    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        Projects project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        Long profileId = project.getProfile().getId();

        projectRepository.deleteById(id);
        return "redirect:/admin/projects/" + profileId; // ✅ Stay within profile context
    }
}