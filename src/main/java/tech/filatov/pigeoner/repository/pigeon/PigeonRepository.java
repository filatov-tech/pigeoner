package tech.filatov.pigeoner.repository.pigeon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.PigeonLabelDto;

import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PigeonRepository extends JpaRepository<Pigeon, Long>, PigeonRepositoryCustom {

    Optional<Pigeon> findByIdAndOwnerId(long id, long userId);

    @Query("""
    SELECT new tech.filatov.pigeoner.dto.PigeonShallowDto(
        p.id, p.name, p.ringNumber, p.birthdate, c.name, p.conditionStatus, p.sex,
        p.isOwn, p.countryCode, m.id, m.ringNumber, p.father.id, p.mother.id , p.keeper.id, p.section.id)
    FROM Pigeon p
    LEFT OUTER JOIN Color c ON p.color.id = c.id
    LEFT OUTER JOIN Pigeon m ON p.mate.id = m.id
    WHERE p.owner.id = :userId
    """)
    List<PigeonShallowDto> getAllPigeonShallowDto(long userId);

    @Query("""
    SELECT new tech.filatov.pigeoner.dto.PigeonDto(
        p.id, p.name, p.ringNumber, p.birthdate, p.color.id, p.conditionStatus, p.sex,
        p.isOwn, p.countryCode, p.mate.id, p.father.id, p.mother.id, k.id, k.name, p.section.id)
    FROM Pigeon p LEFT OUTER JOIN Keeper k ON p.keeper.id = k.id
    WHERE p.owner.id = :userId AND p.id = :id
    """)
    Optional<PigeonDto> findOneDto(long id, long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.PigeonLabelDto(p.id, p.name, p.ringNumber, p.sex, p.section.id) " +
            "FROM Pigeon p " +
            "WHERE p.owner.id = :userId AND p.isOwn IS TRUE")
    List<PigeonLabelDto> getAllLabelDto(long userId);

    @Query(nativeQuery = true)
    List<PigeonDto> getWithAncestorsById(long id, long userId);

    @Query(nativeQuery = true)
    List<PigeonDto> getWithAllAncestorsById(long id, long userId);

    @Query(nativeQuery = true)
    List<PigeonDto> getWithAllDescendantsById(long id, long userId);

    @Query("""
        SELECT p FROM Pigeon p WHERE (p.father.id = :id OR p.mother.id = :id) AND p.owner.id = :userId
    """)
    Set<Pigeon> getAllDirectChildren(long id, long userId);

    List<Pigeon> getAllBySectionIdAndOwnerId(long sectionId, long userId);

    List<Pigeon> getAllBySectionIdInAndOwnerId(List<Long> ids, long userId);

    @Modifying
    @Query("DELETE FROM Pigeon p WHERE p.id = :id AND p.owner.id = :userId")
    int deleteByIdAndOwnerId(long id, long userId);

    @Query("SELECT COUNT(p) FROM Pigeon p JOIN p.images WHERE p.id = :id AND p.owner.id = :userId")
    int getImagesNumber(long id, long userId);

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.PigeonLabelDto(p.id, p.name, p.ringNumber, p.sex, p.section.id)
        FROM Pigeon p
        WHERE p.section.id = :sectionId AND p.owner.id = :userId
    """)
    List<PigeonLabelDto> getLabelDtoBySectionId(long sectionId, long userId);
}
