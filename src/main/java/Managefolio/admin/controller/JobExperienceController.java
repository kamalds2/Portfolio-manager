package Managefolio.admin.controller;

import Managefolio.admin.model.JobExperience;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.JobExperienceRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/jobs")
public class JobExperienceController {

    private final JobExperienceRepository jobRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public JobExperienceController(JobExperienceRepository jobRepository, ProfileRepository profileRepository) {
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
    }

    // 🌐 View: List jobs for a specific profile
    @GetMapping("/{profileId}")
    public String listJobs(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        List<JobExperience> jobs = jobRepository.findByProfileId(profileId);

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("jobs", jobs);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "jobs/list");
        return "layout/base";
    }

    // 🌐 View: Show form for new job under a profile
    @GetMapping("/new/{profileId}")
    public String showCreateForm(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        JobExperience job = new JobExperience();
        job.setProfile(profile);

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("job", job);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "jobs/form");
        return "layout/base";
    }

    // 🌐 View: Show form for editing job
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        JobExperience job = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        Profile profile = job.getProfile();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("job", job);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "jobs/form");
        return "layout/base";
    }

    // 📝 Save job (create or update)
    @PostMapping({"/add", "/edit/{id}"})
    public String saveJob(@ModelAttribute JobExperience job) {
        jobRepository.save(job);
        return "redirect:/admin/jobs/" + job.getProfile().getId();
    }

    // ❌ Delete job
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        JobExperience job = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        Long profileId = job.getProfile().getId();

        jobRepository.deleteById(id);
        return "redirect:/admin/jobs/" + profileId;
    }
}