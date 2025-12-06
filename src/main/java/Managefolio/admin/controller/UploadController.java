package Managefolio.admin.controller;

import Managefolio.admin.model.Profile;
import Managefolio.admin.repository.ProfileRepository;
import Managefolio.admin.services.UploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/portfolio/profiles")
public class UploadController {

    private final ProfileRepository profileRepository;
    private final UploadService uploadService;

    public UploadController(ProfileRepository profileRepository, UploadService uploadService) {
        this.profileRepository = profileRepository;
        this.uploadService = uploadService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Profile> opt = profileRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
        Profile profile = opt.get();

        try {
            String publicPath = uploadService.storeProfileImage(id, file);
            profile.setProfileImage(publicPath);
            profileRepository.save(profile);
            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}/upload-resume")
    public ResponseEntity<?> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Profile> opt = profileRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
        Profile profile = opt.get();

        try {
            String publicPath = uploadService.storeResume(id, file);
            profile.setResumeUrl(publicPath);
            profileRepository.save(profile);
            return ResponseEntity.ok(profile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file");
        }
    }
}
