package tech.filatov.pigeoner.repository.pigeon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tech.filatov.pigeoner.dto.PigeonLabelDto;

import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.List;
import java.util.Optional;

public interface PigeonRepository extends JpaRepository<Pigeon, Long>, PigeonRepositoryCustom {

    Optional<Pigeon> findByIdAndOwnerId(long id, long userId);

    @Query("""
    SELECT new tech.filatov.pigeoner.dto.PigeonShallowDto(
        p.id, p.name, p.ringNumber, p.birthdate, c.name, p.conditionStatus, p.sex,
        p.isOwn, m.id, m.ringNumber, p.father.id, p.mother.id , p.keeper.id, p.section.id)
    FROM Pigeon p
    LEFT OUTER JOIN Color c ON p.color.id = c.id
    LEFT OUTER JOIN Pigeon m ON p.mate.id = m.id
    WHERE p.owner.id = :userId
    """)
    List<PigeonShallowDto> getAllPigeonShallowDto(long userId);

    @Query("""
    SELECT new tech.filatov.pigeoner.dto.PigeonDto(
        p.id, p.name, p.ringNumber, p.birthdate, p.color.id, p.conditionStatus, p.sex,
        p.isOwn, p.mate.id, p.father.id, p.mother.id, k.id, k.name, p.section.id)
    FROM Pigeon p LEFT OUTER JOIN Keeper k ON p.keeper.id = k.id
    WHERE p.owner.id = :userId AND p.id = :id
    """)
    Optional<PigeonDto> findOneDto(long id, long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.PigeonLabelDto(p.id, p.ringNumber, p.sex, p.section.id) " +
            "FROM Pigeon p " +
            "WHERE p.owner.id = :userId AND p.isOwn IS TRUE")
    List<PigeonLabelDto> getAllLabelDto(long userId);

    @Query(nativeQuery = true)
    List<PigeonDto> getWithAncestorsById(long id, long userId);

    @Modifying
    @Query("DELETE FROM Pigeon p WHERE p.id = :id AND p.owner.id = :userId")
    int deleteByIdAndOwnerId(long id, long userId);
}
