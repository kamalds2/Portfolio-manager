package Managefolio.admin.repository;

import Managefolio.admin.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    long countByActiveTrue(); // ✅ returns long instead of Object

    Profile findByUserId(Long userId); // ✅ clear parameter naming
}