package Managefolio.admin.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {

    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024; // 5 MB
    private static final long MAX_RESUME_SIZE = 10L * 1024 * 1024; // 10 MB

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final Set<String> ALLOWED_RESUME_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public String storeProfileImage(Long profileId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is empty");
        if (file.getSize() > MAX_IMAGE_SIZE) throw new IllegalArgumentException("Image too large");
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) throw new IllegalArgumentException("Unsupported image type");

    String original = file.getOriginalFilename();
    String cleaned = StringUtils.cleanPath(original == null ? "" : original);
    String ext = getExtension(cleaned);
    String filename = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

        Path uploadDir = Path.of("uploads", "images", String.valueOf(profileId));
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/images/" + profileId + "/" + filename;
    }

    public String storeResume(Long profileId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is empty");
        if (file.getSize() > MAX_RESUME_SIZE) throw new IllegalArgumentException("Resume too large");
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_RESUME_TYPES.contains(contentType)) throw new IllegalArgumentException("Unsupported resume type");

    String original = file.getOriginalFilename();
    String cleaned = StringUtils.cleanPath(original == null ? "" : original);
    String ext = getExtension(cleaned);
    String filename = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

        Path uploadDir = Path.of("uploads", "resumes", String.valueOf(profileId));
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/resumes/" + profileId + "/" + filename;
    }

    public String storeProjectImage(Long projectId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is empty");
        if (file.getSize() > MAX_IMAGE_SIZE) throw new IllegalArgumentException("Image too large");
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) throw new IllegalArgumentException("Unsupported image type");

        String original = file.getOriginalFilename();
        String cleaned = StringUtils.cleanPath(original == null ? "" : original);
        String ext = getExtension(cleaned);
        String filename = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

        Path uploadDir = Path.of("uploads", "projects", String.valueOf(projectId));
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/projects/" + projectId + "/" + filename;
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int i = filename.lastIndexOf('.');
        if (i <= 0) return "";
        return filename.substring(i + 1);
    }
}
