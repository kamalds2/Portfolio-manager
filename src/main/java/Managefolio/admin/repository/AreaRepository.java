package Managefolio.admin.repository;

import Managefolio.admin.model.AreaOfExpertise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<AreaOfExpertise, Long> {
    List<AreaOfExpertise> findByProfileId(Long profileId);
}
