package Managefolio.admin.repository;

import Managefolio.admin.model.JobExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobExperienceRepository extends JpaRepository<JobExperience, Long> {

    List<JobExperience> findByActiveTrue(); // Optional: if you track active jobs separately

    List<JobExperience> findByProfileId(Long profileId); // All jobs for a profile

    JobExperience findTopByProfileIdAndEndDateIsNull(Long profileId); // Current job only
}