package Managefolio.admin.model;

	import jakarta.persistence.*;
	import lombok.*;
	import java.time.LocalDateTime;
	import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

	@Entity
	@Table(name = "tbl_profile")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public class Profile {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String fullName;
	    private String title;
	    @Column(columnDefinition = "TEXT")
	    private String bio;
	    private String location;
	    private String email;
	    private String phone;
	    private String profileImage;
	    private String resumeUrl;
	    private boolean active;

	    @CreatedDate
	    private LocalDateTime createdAt;

	    @LastModifiedDate
	    private LocalDateTime updatedAt;

	    @CreatedBy
	    private Long createdBy;

	    @LastModifiedBy
	    private Long updatedBy;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }

	    // Relationships
	    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
	    private List<Projects> projects;

	    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
	    private List<JobExperience> experiences;

	    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Skill> skills;

	    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Education> educationList;

	    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<About> aboutSections;
	}