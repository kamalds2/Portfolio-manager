package Managefolio.admin.repository;

import Managefolio.admin.model.Profile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    long countByActiveTrue(); // ✅ returns long instead of Object

    List<Profile> findByUserId(Long userId); // Returns list to handle duplicates
    
    List<Profile> findByActiveTrue();// ✅ clear parameter naming

	Optional<Profile> findByIdAndActiveTrue(Long id);
}