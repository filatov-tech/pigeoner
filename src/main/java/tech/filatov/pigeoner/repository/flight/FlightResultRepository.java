package tech.filatov.pigeoner.repository.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightResultDto;
import tech.filatov.pigeoner.model.flight.FlightResult;

import java.util.List;
import java.util.Optional;

public interface FlightResultRepository extends JpaRepository<FlightResult, Long>, FlightResultRepositoryCustom {

    Optional<FlightResult> findByIdAndOwnerId(long id, long userId);

    List<FlightResult> findAllByFlightIdAndOwnerId(long flightId, long userId);

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.FlightResultDto(
        fr.id, fr.flight.id, p.id, p.ringNumber, p.name, fr.position, fr.arrivalTime, fr.isPass,
        fr.winPoints, fr.averageSpeed, fr.condition, p.keeper.name)
        FROM FlightResult fr JOIN Pigeon p ON fr.pigeon.id = p.id
        WHERE fr.id = :id AND fr.owner.id = :userId AND p.owner.id = :userId
    """)
    Optional<FlightResultDto> findDto(long id, long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.FlightResultDto(" +
            "fr.pigeon.id, lp.id, lp.name, lp.distance, fr.position, fl.totalParticipants, fr.arrivalTime, fr.isPass) " +
            "FROM FlightResult fr " +
            "JOIN Flight fl ON fr.flight.id = fl.id " +
            "JOIN LaunchPoint lp ON fl.launchPoint.id = lp.id " +
            "WHERE fr.pigeon.id = :id AND fr.owner.id = :userId AND fl.owner.id = :userId AND lp.owner.id = :userId")
    List<FlightResultDto> getAllDtoByPigeonId(long id, long userId);

    @Query("""
            SELECT new tech.filatov.pigeoner.dto.FlightResultDto(
            fr.id, fr.flight.id, p.id, p.ringNumber, p.name, fr.position, fr.arrivalTime, fr.isPass, fr.winPoints, fr.averageSpeed, fr.condition, p.keeper.name)
            FROM FlightResult fr JOIN Pigeon p ON fr.pigeon.id = p.id
            WHERE fr.flight.id = :id AND fr.owner.id = :userId AND p.owner.id = :userId
            ORDER BY fr.arrivalTime
    """)
    List<FlightResultDto> getAllDtoByFlightId(long id, long userId);

    @Query("""
        SELECT count(fr)
        FROM FlightResult fr
        LEFT OUTER JOIN Pigeon p ON fr.pigeon.id = p.id
        WHERE fr.flight.id = :id AND fr.owner.id = :userId AND p.isOwn IS TRUE
    """)
    int getNumberOfMyParticipantsByFlightId(long id, long userId);

    @Modifying
    @Query("""
        DELETE FROM FlightResult fr WHERE fr.id = :id AND fr.owner.id = :userId
    """)
    int delete(long id, long userId);
}
