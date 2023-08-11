package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.filatov.pigeoner.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findById(long id);
}
