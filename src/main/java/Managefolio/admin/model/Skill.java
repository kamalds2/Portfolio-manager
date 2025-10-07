package Managefolio.admin.model;

import jakarta.persistence.*;
import lombok.*;



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
	    private Profile profile;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;
	}