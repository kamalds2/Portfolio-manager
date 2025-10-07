package Managefolio.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Managefolio.admin.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}