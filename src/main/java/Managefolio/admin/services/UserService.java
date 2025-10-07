package Managefolio.admin.services;
import Managefolio.admin.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void delete(Long id);
    Optional<User> findByUsername(String username);
}
