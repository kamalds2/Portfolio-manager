package Managefolio.admin.repository;

import Managefolio.admin.model.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Projects, Long> {

    List<Projects> findByActiveTrue(); // Optional: if your Projects entity has an 'active' boolean

    List<Projects> findByProfileId(Long profileId); // All projects for a profile

    List<Projects> findByProfileIdAndStatus(Long profileId, String status); // Filtered by status (e.g., "ACTIVE")
}