package tech.filatov.pigeoner.repository.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.FlightDto;
import tech.filatov.pigeoner.model.flight.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.FlightDto(
        f.id, lp.id, lp.name, lp.distance, f.totalParticipants,
        f.departure, f.passingThreshold, f.flightType, f.isSynced)
        FROM Flight f
        JOIN LaunchPoint lp ON f.launchPoint.id = lp.id
        WHERE f.owner.id = :userId
    """)
    List<FlightDto> getAllDto(long userId);

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.FlightDto(
        f.id, lp.id, lp.name, lp.distance, f.totalParticipants,
        f.departure, f.passingThreshold, f.flightType, f.isSynced)
        FROM Flight f
        JOIN LaunchPoint lp ON f.launchPoint.id = lp.id
        WHERE f.id = :id AND f.owner.id = :userId
    """)
    Optional<FlightDto> findDtoById(long id, long userId);

    Optional<Flight> findOneByIdAndOwnerId(long id, long userId);

    @Modifying
    @Query("""
        DELETE FROM Flight f WHERE f.id = :id AND f.owner.id = :userId
    """)
    int delete(long id, long userId);
}
