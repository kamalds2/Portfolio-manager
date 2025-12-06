package Managefolio.admin.controller;

import Managefolio.admin.model.Projects;
import Managefolio.admin.model.Profile;
import Managefolio.admin.model.User;
import Managefolio.admin.repository.ProjectRepository;
import Managefolio.admin.repository.ProfileRepository;
import Managefolio.admin.repository.UserRepository;
import Managefolio.admin.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Controller
@RequestMapping("/admin/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UploadService uploadService;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, ProfileRepository profileRepository, 
                           UserRepository userRepository, UploadService uploadService) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.uploadService = uploadService;
    }

    private void addUserProfileForNavigation(Model model, Authentication authentication, boolean isAdmin) {
        if (!isAdmin) {
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                List<Profile> userProfiles = profileRepository.findByUserId(user.getId());
                Profile userProfile = userProfiles.isEmpty() ? null : userProfiles.get(0);
                model.addAttribute("userProfile", userProfile);
            }
        }
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
        addUserProfileForNavigation(model, authentication, isAdmin);

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
    public String saveProject(@ModelAttribute Projects project,
                             @RequestParam(name = "projectImages", required = false) MultipartFile[] projectImages,
                             org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        
        // Save project first to get ID for file uploads
        Projects savedProject = projectRepository.save(project);
        
        // Handle multiple image uploads
        if (projectImages != null && projectImages.length > 0) {
            List<String> uploadedImageUrls = new java.util.ArrayList<>();
            
            // Keep existing images if editing
            if (savedProject.getImageUrls() != null && !savedProject.getImageUrls().trim().isEmpty()) {
                uploadedImageUrls.addAll(savedProject.getImageUrlsList());
            }
            
            try {
                for (MultipartFile file : projectImages) {
                    if (file != null && !file.isEmpty()) {
                        String imagePath = uploadService.storeProjectImage(savedProject.getId(), file);
                        uploadedImageUrls.add(imagePath);
                    }
                }
                
                // Update project with new image URLs
                savedProject.setImageUrlsList(uploadedImageUrls);
                projectRepository.save(savedProject);
                
            } catch (Exception e) {
                // Log error but continue - project saved without new images
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("errorMessage", "Project saved but image upload failed: " + e.getMessage());
                return "redirect:/admin/projects/" + savedProject.getProfile().getId();
            }
        }
        
        boolean isNew = project.getId() == null;
        redirectAttributes.addFlashAttribute("successMessage", 
            isNew ? "Project added successfully!" : "Project updated successfully!");
        
        return "redirect:/admin/projects/" + savedProject.getProfile().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            Projects project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
            Long profileId = project.getProfile().getId();
            projectRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully!");
            return "redirect:/admin/projects/" + profileId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete project: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}