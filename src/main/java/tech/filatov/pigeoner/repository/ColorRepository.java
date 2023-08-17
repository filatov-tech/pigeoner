package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.ColorDto;
import tech.filatov.pigeoner.model.pigeon.Color;

import java.util.List;
import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> getColorByNameAndOwnerId(String name, long userId);

    Optional<Color> findByIdAndOwnerId(long id, long userId);

    @Query("""
    SELECT c FROM Pigeon p LEFT OUTER JOIN Color c ON p.color.id = c.id
    WHERE p.id = :id AND p.owner.id = :userId
    """)
    Optional<Color> findColorByPigeonId(long id, long userId);

    @Query("""
    SELECT new tech.filatov.pigeoner.dto.ColorDto(c.id, c.name)
    FROM Color c
    WHERE c.id = :id AND c.owner.id = :userId
    """)
    Optional<ColorDto> getDtoByIdAndOwnerId(long id, long userId);

    @Query("""
    SELECT new tech.filatov.pigeoner.dto.ColorDto(c.id, c.name)
    FROM Color c
    WHERE c.owner.id = :userId
    """)
    List<ColorDto> getAllDtoByOwnerId(long userId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM Color c WHERE c.id = :id AND c.owner.id = :userId
    """)
    int deleteByIdAndOwnerId(long id, long userId);
}
