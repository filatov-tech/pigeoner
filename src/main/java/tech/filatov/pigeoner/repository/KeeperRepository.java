package tech.filatov.pigeoner.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.KeeperDto;
import tech.filatov.pigeoner.model.Keeper;

import java.util.List;
import java.util.Optional;

public interface KeeperRepository extends JpaRepository<Keeper, Long> {

    Optional<Keeper> findByIdAndOwnerId(long id, long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.KeeperDto(k.id, k.name) FROM Keeper k WHERE k.owner.id = :userId ORDER BY k.name")
    List<KeeperDto> getAllDto(long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.KeeperDto(k.id, k.name) FROM Keeper k " +
            "WHERE k.id = :id AND k.owner.id = :userId")
    KeeperDto getDto(long id, long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.KeeperDto(k.id, k.name) FROM User u JOIN Keeper k ON u.keeper.id = k.id " +
            "WHERE u.id = :userId")
    KeeperDto getDto(long userId);
}
