package tech.filatov.pigeoner.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tech.filatov.pigeoner.dto.SectionDto;
import tech.filatov.pigeoner.model.dovecote.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByIdAndOwnerId(long id, long userId);

    @Query("""
            SELECT new tech.filatov.pigeoner.dto.SectionDto(s.id, s.name, s.parent.id, s.type)
            FROM Section s WHERE s.id = :id AND s.owner.id = :userId
            """)
    Optional<SectionDto> findDtoByIdAndOwnerId(long id, long userId);

    @EntityGraph(attributePaths = {"pigeons"})
    Optional<Section> findWithPigeonsByIdAndOwnerId(long id, long userId);

    @EntityGraph(attributePaths = {"parent"})
    Optional<Section> findWithParentByIdAndOwnerId(long id, long userId);

    @EntityGraph(attributePaths = {"parent"})
    List<Section> getAllByIdInAndOwnerId(List<Long> ids, long userId);

    @Modifying
    @Query("""
            DELETE FROM Section s WHERE s.id = :id AND s.owner.id = :userId
            """)
    int deleteById(long id, long userId);

    @Query("SELECT s FROM Section s WHERE s.parent IS NULL AND s.owner.id = :userId")
    List<Section> getTopLevel(long userId);

    @Query(nativeQuery = true)
    List<SectionDto> getTopLevelCommonSectionInfo(long userId);

    @Query(nativeQuery = true)
    List<SectionDto> getCommonSectionInfoById(int id, long userId);

    @Query("""
        SELECT new tech.filatov.pigeoner.dto.SectionDto(s.id, s.name, s.parent.id, s.type)
        FROM Section s
        WHERE s.owner.id = :userId
    """)
    List<SectionDto> getAll(long userId);

    @Query("""
            SELECT new tech.filatov.pigeoner.dto.SectionDto(s.id, s.name, s.parent.id)
            FROM Section s
            WHERE s.type IN (
                tech.filatov.pigeoner.model.dovecote.SectionType.DOVECOTE,
                tech.filatov.pigeoner.model.dovecote.SectionType.ROOM
            )
            AND s.owner.id = :userId
    """)
    List<SectionDto> getAllWithoutNests(long userId);

    @Query(nativeQuery = true)
    List<Long> getIdListOfAllDescendantsById(long id, long userId);

    @Query(nativeQuery = true)
    List<SectionDto> getAllWithInfo(long userId);
}
