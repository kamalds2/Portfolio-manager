package Managefolio.admin.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tbl_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String techStack;
    private String imageUrl; // Keep for backward compatibility
    
    @Column(columnDefinition = "TEXT")
    private String imageUrls; // Multiple images stored as comma-separated string
    
    private String projectUrl;
    private String category;
    private String status;
    private boolean active;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Integer updatedBy;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile profile;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Utility methods for handling multiple images
    public java.util.List<String> getImageUrlsList() {
        if (imageUrls == null || imageUrls.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.asList(imageUrls.split(","))
                .stream()
                .map(String::trim)
                .filter(url -> !url.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }

    public void setImageUrlsList(java.util.List<String> imageUrlsList) {
        if (imageUrlsList == null || imageUrlsList.isEmpty()) {
            this.imageUrls = null;
        } else {
            this.imageUrls = String.join(",", imageUrlsList);
        }
    }

    public void addImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return;
        }
        
        java.util.List<String> currentUrls = getImageUrlsList();
        currentUrls.add(imageUrl.trim());
        setImageUrlsList(currentUrls);
    }

    public void removeImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return;
        }
        
        java.util.List<String> currentUrls = getImageUrlsList();
        currentUrls.removeIf(url -> url.equals(imageUrl.trim()));
        setImageUrlsList(currentUrls);
    }

}
