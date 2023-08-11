package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.model.pigeon.Color;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> getColorByNameAndOwnerId(String name, long userId);

    @Query("""
    SELECT c FROM Pigeon p LEFT OUTER JOIN Color c ON p.color.id = c.id
    WHERE p.id = :id AND p.owner.id = :userId
    """)
    Color findColorByPigeonId(long id, long userId);
}
