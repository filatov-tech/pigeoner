package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.filatov.pigeoner.model.pigeon.Color;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> getColorByNameAndOwnerId(String name, long userId);
}
