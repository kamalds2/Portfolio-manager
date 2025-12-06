package Managefolio.admin.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;



@Entity
@Table(name = "tbl_skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
	
	public enum SkillLevel {
	    BEGINNER,
	    INTERMEDIATE,
	    EXPERT
	}


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private SkillLevel level;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "profile_id", nullable = false)
		@JsonBackReference
		private Profile profile;

	    
	}