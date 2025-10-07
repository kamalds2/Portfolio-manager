package Managefolio.admin.repository;

import Managefolio.admin.model.About;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AboutRepository extends JpaRepository<About, Long> {
    Optional<About> findByProfileId(Long profileId);
}