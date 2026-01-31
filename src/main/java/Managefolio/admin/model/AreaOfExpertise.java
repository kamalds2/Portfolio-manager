package Managefolio.admin.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaOfExpertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonBackReference
    private Profile profile;

    @Column(columnDefinition = "TEXT")
    private String skills; // comma-separated skill names

    // Helper method to get skills as list
    public List<String> getSkillsList() {
        if (skills == null || skills.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(skills.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    // Helper method to set skills from list
    public void setSkillsList(List<String> skillsList) {
        if (skillsList == null || skillsList.isEmpty()) {
            this.skills = null;
        } else {
            this.skills = String.join(", ", skillsList);
        }
    }
}
