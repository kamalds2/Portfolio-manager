package Managefolio.admin.controller;

import Managefolio.admin.model.Skill;
import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.SkillRepository;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/skills")
@PreAuthorize("hasRole('ADMIN')")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProfileRepository profileRepository;

    // 🌐 Admin View: List Skills for a Profile
    @GetMapping("/{profileId}")
    public String listSkills(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        model.addAttribute("skills", skillRepository.findByProfileId(profileId));
        model.addAttribute("activeProfile", profile); // ✅ Enables dynamic header
        model.addAttribute("viewName", "skill/list");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 🌐 Admin View: Show Form to Create Skill
    @GetMapping("/new/{profileId}")
    public String createSkillForm(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        Skill skill = new Skill();
        skill.setProfile(profile);
        model.addAttribute("skill", skill);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "skill/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // 📝 Admin View: Save Skill (Create or Update)
    @PostMapping("/add")
    public String saveSkill(@ModelAttribute Skill skill) {
        skillRepository.save(skill);
        return "redirect:/admin/skills/" + skill.getProfile().getId(); // ✅ Stay in profile context
    }

    // 🌐 Admin View: Show Form to Edit Skill
    @GetMapping("/edit/{id}")
    public String editSkill(@PathVariable Long id, Model model) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid skill ID: " + id));
        Profile profile = skill.getProfile();
        model.addAttribute("skill", skill);
        model.addAttribute("activeProfile", profile); // ✅ For header context
        model.addAttribute("viewName", "skill/form");
        model.addAttribute("isAdmin", true);
        return "layout/base";
    }

    // ❌ Admin View: Delete Skill
    @GetMapping("/delete/{id}")
    public String deleteSkill(@PathVariable Long id) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid skill ID: " + id));
        Long profileId = skill.getProfile().getId();
        skillRepository.deleteById(id);
        return "redirect:/admin/skills/" + profileId; // ✅ Stay in profile context
    }
}