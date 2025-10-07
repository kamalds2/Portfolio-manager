package Managefolio.admin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_education")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
	

	public enum EducationLevel {
		SCHOOL,
		INTERMEDIATE,
	    DIPLOMA,
	    BACHELOR,
	    MASTER,
	    DOCTORATE,
	    CERTIFICATION
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution;

    private String degree;

    private String fieldOfStudy;

    private int startYear;

    private int endYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}