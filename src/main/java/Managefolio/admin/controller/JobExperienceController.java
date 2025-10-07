package Managefolio.admin.controller;

import Managefolio.admin.model.JobExperience;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.JobExperienceRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/jobs")
@PreAuthorize("hasRole('ADMIN')")
public class JobExperienceController {

    private final JobExperienceRepository jobRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public JobExperienceController(JobExperienceRepository jobRepository, ProfileRepository profileRepository) {
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
    }

    // 🌐 Admin View: List jobs for a specific profile
    @GetMapping("/{profileId}")
    public String listJobs(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        List<JobExperience> jobs = jobRepository.findByProfileId(profileId);

        model.addAttribute("jobs", jobs);
        model.addAttribute("activeProfile", profile); // ✅ Enables dynamic header
        model.addAttribute("viewName", "jobs/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show form for new job under a profile
    @GetMapping("/new/{profileId}")
    public String showCreateForm(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        JobExperience job = new JobExperience();
        job.setProfile(profile); // ✅ Bind profile to new job

        model.addAttribute("job", job);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "jobs/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show form for editing job
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        JobExperience job = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        Profile profile = job.getProfile();

        model.addAttribute("job", job);
        model.addAttribute("activeProfile", profile); // ✅ For header context
        model.addAttribute("viewName", "jobs/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 📝 Admin View: Save job (create or update)
    @PostMapping({"/add", "/edit/{id}"})
    public String saveJob(@ModelAttribute JobExperience job) {
        jobRepository.save(job);
        return "redirect:/admin/jobs/" + job.getProfile().getId(); // ✅ Stay in profile context
    }

    // ❌ Admin View: Delete job
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        JobExperience job = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        Long profileId = job.getProfile().getId();

        jobRepository.deleteById(id);
        return "redirect:/admin/jobs/" + profileId; // ✅ Stay in profile context
    }
}