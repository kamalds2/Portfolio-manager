package Managefolio.admin.services;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {

    private static final String BUCKET_NAME = "portfolio-uploads"; // GCS bucket name
    private final Storage storage = StorageOptions.getDefaultInstance().getService();

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
        String filename = "profile_" + profileId + "_" + UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        String objectName = "images/" + profileId + "/" + filename;

        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, objectName)
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, objectName);
    }

    public String storeResume(Long profileId, MultipartFile file) throws IOException {
        return storeResume(profileId, file, null);
    }

    public String storeResume(Long profileId, MultipartFile file, String name) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is empty");
        if (file.getSize() > MAX_RESUME_SIZE) throw new IllegalArgumentException("Resume too large");
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_RESUME_TYPES.contains(contentType)) throw new IllegalArgumentException("Unsupported resume type");

        String original = file.getOriginalFilename();
        String cleaned = StringUtils.cleanPath(original == null ? "" : original);
        String ext = getExtension(cleaned);

        String base;
        if (name != null && !name.isBlank()) {
            base = sanitizeName(name);
        } else {
            String origBase = cleaned.isEmpty() ? "" : cleaned.replaceAll("\\.[^.]+$", "");
            base = origBase.isBlank() ? UUID.randomUUID().toString() : sanitizeName(origBase);
        }

        String filename = "resume_" + base + "_" + UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        String objectName = "resumes/" + profileId + "/" + filename;

        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, objectName)
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, objectName);
    }

    private String sanitizeName(String input) {
        if (input == null) return "";
        // Trim, replace whitespace and disallowed chars with underscore, collapse multiple underscores
        String s = input.trim().replaceAll("\\s+", "_");
        s = s.replaceAll("[^A-Za-z0-9_\\-]", "_");
        s = s.replaceAll("_+", "_");
        if (s.length() > 100) s = s.substring(0, 100);
        return s.toLowerCase();
    }

    public String storeProjectImage(Long projectId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is empty");
        if (file.getSize() > MAX_IMAGE_SIZE) throw new IllegalArgumentException("Image too large");
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) throw new IllegalArgumentException("Unsupported image type");

        String original = file.getOriginalFilename();
        String cleaned = StringUtils.cleanPath(original == null ? "" : original);
        String ext = getExtension(cleaned);
        String filename = "project_" + projectId + "_" + UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        String objectName = "projects/" + projectId + "/" + filename;

        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, objectName)
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, objectName);
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int i = filename.lastIndexOf('.');
        if (i <= 0) return "";
        return filename.substring(i + 1);
    }
}
