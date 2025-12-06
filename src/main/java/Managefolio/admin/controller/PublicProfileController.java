package Managefolio.admin.controller;

import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/portfolio")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5175"}) // React frontend - support both Vite ports
public class PublicProfileController {

    private final ProfileRepository profileRepository;

    @Autowired
    public PublicProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // ✅ Updated: Get a specific active profile by ID (returns 200 or 404 for React frontend)
    @GetMapping("/profiles/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileRepository.findByIdAndActiveTrue(id);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profiles") // 🌐 Public API: Get id, fullName, and profileImage of active profiles
    public List<SimpleProfile> getActiveProfileSummaries() {
        return profileRepository.findByActiveTrue().stream()
            .map(profile -> new SimpleProfile(
                profile.getId(),
                profile.getFullName(),
                profile.getProfileImage()
            ))
            .collect(Collectors.toList());
    }

    // Inner static class for lightweight response
    public static class SimpleProfile {
        private Long id;
        private String fullName;
        private String profileImage;

        public SimpleProfile(Long id, String fullName, String profileImage) {
            this.id = id;
            this.fullName = fullName;
            this.profileImage = profileImage;
        }

        public Long getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getProfileImage() {
            return profileImage;
        }
    }
}