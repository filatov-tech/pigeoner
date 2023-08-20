package tech.filatov.pigeoner.repository.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.LaunchPointDto;
import tech.filatov.pigeoner.model.flight.LaunchPoint;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaunchPointRepository extends JpaRepository<LaunchPoint, Long> {
    Optional<LaunchPoint> findOneByIdAndOwnerId(long id, long userId);
    List<LaunchPoint> findAllByOwnerId(long userId);

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.LaunchPointDto(l.id, l.name, l.distance) FROM LaunchPoint l
        WHERE l.id = :id AND l.owner.id = :userId
    """)
    Optional<LaunchPointDto> findOneDto(long id, long userId);

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.LaunchPointDto(l.id, l.name, l.distance) FROM LaunchPoint l
        WHERE l.owner.id = :userId
    """)
    List<LaunchPointDto> findAllDtoByOwnerId(long userId);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM LaunchPoint l WHERE l.id = :id AND l.owner.id = :userId
    """)
    int delete(long id, long userId);
}
