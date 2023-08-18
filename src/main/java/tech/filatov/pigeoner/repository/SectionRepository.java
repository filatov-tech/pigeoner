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

    @Modifying
    @Query("""
            DELETE FROM Section s WHERE s.id = :id AND s.owner.id = :userId
            """)
    int delete(long id, long userId);

    @Query("SELECT s FROM Section s WHERE s.parent IS NULL")
    List<Section> getTopLevel();

    @Query(nativeQuery = true)
    List<SectionDto> getTopLevelCommonSectionInfo();

    @Query(nativeQuery = true)
    List<SectionDto> getCommonSectionInfoById(int id);

    @Query("SELECT new tech.filatov.pigeoner.dto.SectionDto(s.id, s.name, s.parent.id) FROM Section s " +
            "WHERE s.owner.id = :userId")
    List<SectionDto> getAll(long userId);

    @Query("SELECT new tech.filatov.pigeoner.dto.SectionDto(s.id, s.name, s.parent.id) FROM Section s " +
            "WHERE s.type IN (" +
            "tech.filatov.pigeoner.model.dovecote.SectionType.DOVECOTE, " +
            "tech.filatov.pigeoner.model.dovecote.SectionType.ROOM)")
    List<SectionDto> getAllWithoutNests();

    @Query(nativeQuery = true)
    List<Long> getIdListOfAllDescendantsById(long id);

    @Query(nativeQuery = true)
    List<SectionDto> getAllWithInfo();
}
