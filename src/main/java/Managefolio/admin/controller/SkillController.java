package Managefolio.admin.controller;

import Managefolio.admin.model.Skill;
import Managefolio.admin.model.Profile;
import Managefolio.admin.model.User;
import Managefolio.admin.repository.SkillRepository;
import Managefolio.admin.repository.ProfileRepository;
import Managefolio.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequestMapping("/admin/skills")
public class SkillController {

    @Autowired private SkillRepository skillRepository;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private UserRepository userRepository;

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
        return "redirect:/admin/skills/" + firstId;
    }

	    @GetMapping("/{profileId}")
	    public String listSkills(@PathVariable Long profileId, Model model, Authentication authentication) {
	        Profile profile = profileRepository.findById(profileId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
	
	        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	        model.addAttribute("isAdmin", isAdmin);
	        addUserProfileForNavigation(model, authentication, isAdmin);
	
	        model.addAttribute("skills", skillRepository.findByProfileId(profileId));
	        model.addAttribute("activeProfile", profile);
	        model.addAttribute("viewName", "skill/list");
	        return "layout/base";
	    }

    @GetMapping("/new/{profileId}")
    public String createSkillForm(@PathVariable Long profileId, Model model, Authentication authentication) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        addUserProfileForNavigation(model, authentication, isAdmin);

        Skill skill = new Skill();
        skill.setProfile(profile);

        model.addAttribute("skill", skill);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "skill/form");
        return "layout/base";
    }

    @PostMapping("/add")
    public String saveSkill(@ModelAttribute Skill skill, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        skillRepository.save(skill);
        return "redirect:/admin/skills/" + skill.getProfile().getId();
    }

    @GetMapping("/edit/{id}")
    public String editSkill(@PathVariable Long id, Model model, Authentication authentication) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid skill ID: " + id));
        Profile profile = skill.getProfile();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        addUserProfileForNavigation(model, authentication, isAdmin);

        model.addAttribute("skill", skill);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "skill/form");
        return "layout/base";
    }

    @GetMapping("/delete/{id}")
    public String deleteSkill(@PathVariable Long id) {
        Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid skill ID: " + id));
        Long profileId = skill.getProfile().getId();

        skillRepository.deleteById(id);
        return "redirect:/admin/skills/" + profileId;
    }
}