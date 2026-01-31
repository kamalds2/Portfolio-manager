package Managefolio.admin.controller;

import Managefolio.admin.model.AreaOfExpertise;
import Managefolio.admin.model.Profile;
import Managefolio.admin.model.Skill;
import Managefolio.admin.repository.AreaRepository;
import Managefolio.admin.repository.ProfileRepository;
import Managefolio.admin.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/about/{profileId}/areas")
@PreAuthorize("hasRole('ADMIN')")
public class AreaController {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public String listAreas(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        List<AreaOfExpertise> areas = areaRepository.findByProfileId(profileId);
        model.addAttribute("areas", areas);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "about/areas/list");
        return "layout/base";
    }

    @GetMapping("/new")
    public String newArea(@PathVariable Long profileId, Model model) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        AreaOfExpertise area = AreaOfExpertise.builder().profile(profile).build();
        model.addAttribute("area", area);
        model.addAttribute("skillsText", "");
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "about/areas/form");
        return "layout/base";
    }

    @GetMapping("/{areaId}/edit")
    public String editArea(@PathVariable Long profileId, @PathVariable Long areaId, Model model) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        AreaOfExpertise area = areaRepository.findById(areaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid area ID: " + areaId));
        String skillsText = String.join("\n", area.getSkillsList());
        model.addAttribute("area", area);
        model.addAttribute("skillsText", skillsText);
        model.addAttribute("activeProfile", profile);
        model.addAttribute("viewName", "about/areas/form");
        return "layout/base";
    }

    @PostMapping("/save")
    public String saveArea(@PathVariable Long profileId,
                           @ModelAttribute AreaOfExpertise area,
                           @RequestParam(required = false) String skillsText) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile ID: " + profileId));
        area.setProfile(profile);

        // parse skills from textarea and store as comma-separated string
        if (skillsText != null && !skillsText.trim().isEmpty()) {
            List<String> skillNames = Arrays.stream(skillsText.split("\r?\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            area.setSkillsList(skillNames);
        } else {
            area.setSkills(null);
        }

        areaRepository.save(area);
        return "redirect:/admin/about/" + profileId + "/areas";
    }

    @PostMapping("/{areaId}/delete")
    public String deleteArea(@PathVariable Long profileId, @PathVariable Long areaId) {
        areaRepository.deleteById(areaId);
        return "redirect:/admin/about/" + profileId + "/areas";
    }
}
