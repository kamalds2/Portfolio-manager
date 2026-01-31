package Managefolio.admin.model;

	import jakarta.persistence.*;
	import lombok.*;
	import java.time.LocalDateTime;
	import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
	    
		@JsonBackReference
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

		// Relationships (prevent recursive JSON by managing child collections)
		@JsonManagedReference
		@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
		private List<Projects> projects;

		@JsonManagedReference
		@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
		private List<JobExperience> experiences;

		@JsonManagedReference
		@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<Skill> skills;

		@JsonManagedReference
		@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<Education> educationList;

		@JsonManagedReference
		@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<About> aboutSections;

		@JsonManagedReference
		@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<AreaOfExpertise> areas;
	}