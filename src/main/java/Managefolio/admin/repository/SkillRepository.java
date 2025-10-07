package Managefolio.admin.repository;

import Managefolio.admin.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByProfileId(Long profileId);
}