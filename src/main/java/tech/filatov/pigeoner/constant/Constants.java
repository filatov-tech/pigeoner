package tech.filatov.pigeoner.constant;

final public class Constants {
    public static final String BIRTHDATE_TYPE = "dateType";
    public static final String AGE_TYPE = "ageType";
    public static final String YEAR_TYPE = "yearType";
    public static final String VALIDATION_FAILED_MESSAGE = "Ошибка! Такой объект не может быть сохранён";

    public static final String SECTION_DTO_COMMON_NATIVE_QUERY = """
                SELECT ms.id                         AS sect_id,
                       ms.name                       AS sect_name,
                       type                          AS sect_type,
                       ms.parent_id                  AS parent_id,
                       (WITH RECURSIVE pigeon_count AS (SELECT v.pigeon_id, v.location_id, v.section_id, v.type, v.parent_id
                                                        FROM (SELECT p.id       AS pigeon_id,
                                                                     section_id AS location_id,
                                                                     s.id       AS section_id,
                                                                     type,
                                                                     parent_id
                                                              FROM pigeon p
                                                                       LEFT JOIN section s ON FALSE
                                                              WHERE p.user_id = :userId
                                                              UNION
                                                              SELECT p.id       AS pigeon_id,
                                                                     section_id AS location_id,
                                                                     s.id       AS section_id,
                                                                     type,
                                                                     parent_id
                                                              FROM pigeon p
                                                                       RIGHT JOIN section s ON FALSE
                                                              WHERE s.user_id = :userId
                                                              )
                                                                 AS v
                                                        WHERE v.location_id = ms.id
                                                           OR v.parent_id = ms.id
                
                                                        UNION ALL
                
                                                        SELECT w.pigeon_id, w.location_id, w.section_id, w.type, w.parent_id
                                                        FROM (SELECT p.id       AS pigeon_id,
                                                                     section_id AS location_id,
                                                                     s.id       AS section_id,
                                                                     type,
                                                                     parent_id
                                                              FROM pigeon p
                                                                       LEFT JOIN section s ON FALSE
                                                              WHERE p.user_id = :userId
                
                                                              UNION
                
                                                              SELECT p.id       AS pigeon_id,
                                                                     section_id AS location_id,
                                                                     s.id       AS section_id,
                                                                     type,
                                                                     parent_id
                                                              FROM pigeon p
                                                                       RIGHT JOIN section s ON FALSE
                                                              WHERE s.user_id = :userId)
                                                                 AS w
                                                                 INNER JOIN pigeon_count
                                                                            ON (w.pigeon_id IS NOT NULL AND
                                                                                w.location_id = pigeon_count.section_id AND
                                                                                pigeon_count.pigeon_id IS NULL)
                                                                                OR (w.parent_id = pigeon_count.section_id AND
                                                                                    w.pigeon_id IS NULL AND
                                                                                    pigeon_count.pigeon_id IS NULL))
                        SELECT COUNT(*)
                        FROM pigeon_count
                        WHERE pigeon_id IS NOT NULL) AS pigeon_number
                FROM section ms
                WHERE ms.user_id = :userId
            """;
    public static final String SECTION_DTO_ROOT = " WHERE ms.parent_id IS NULL AND ms.user_id = :userId";
    public static final String SECTION_DTO_BY_ID = " WHERE ms.parent_id = :id AND ms.user_id = :userId";
    public static final String SECTION_WITH_DEEP_CHILDREN = """
            WITH RECURSIVE recursive_sections
                               AS (SELECT s.id, s.parent_id
                                   FROM section AS s
                                   WHERE s.parent_id = :id AND s.user_id = :userId
                        
                                   UNION ALL
                        
                                   SELECT sj.id, sj.parent_id
                                   FROM section AS sj
                                            INNER JOIN recursive_sections AS rs ON rs.id = sj.parent_id AND sj.user_id = :userId)
            SELECT rec.id
            FROM recursive_sections rec
            """;
    public static final String PIGEON_WITH_3_LEVEL_ANCESTORS = """
            WITH RECURSIVE pedigree AS (SELECT id,
                                               name,
                                               ring_number,
                                               birthdate,
                                               condition_status,
                                               sex,
                                               is_own,
                                               country_code,
                                               mate_id,
                                               father_id,
                                               mother_id,
                                               keeper_id,
                                               section_id,
                                               0 AS depth
                                        FROM pigeon
                                        WHERE id = :id
                                          AND pigeon.user_id = :userId
                        
                                        UNION
                        
                                        SELECT p.id,
                                               p.name,
                                               p.ring_number,
                                               p.birthdate,
                                               p.condition_status,
                                               p.sex,
                                               p.is_own,
                                               p.country_code,
                                               p.mate_id,
                                               p.father_id,
                                               p.mother_id,
                                               p.keeper_id,
                                               p.section_id,
                                               depth + 1
                                        FROM pigeon p
                                                 JOIN pedigree ped ON ped.father_id = p.id OR ped.mother_id = p.id
                                        WHERE depth < 3)
            SELECT pedigree.id,
                   pedigree.name,
                   ring_number,
                   birthdate,
                   condition_status,
                   sex,
                   is_own,
                   country_code,
                   mate_id,
                   father_id,
                   mother_id,
                   keeper_id,
                   k.name keeper_name,
                   section_id
            FROM pedigree
                      LEFT JOIN keeper k ON keeper_id = k.id AND k.user_id = :userId
            """;
    public static final String PIGEON_WITH_ALL_ANCESTORS = """
            WITH RECURSIVE pedigree AS (SELECT id,
                                               name,
                                               ring_number,
                                               birthdate,
                                               condition_status,
                                               sex,
                                               is_own,
                                               country_code,
                                               mate_id,
                                               father_id,
                                               mother_id,
                                               keeper_id,
                                               section_id,
                                               0 AS depth
                                        FROM pigeon
                                        WHERE id = :id
                                          AND pigeon.user_id = :userId
                        
                                        UNION
                        
                                        SELECT p.id,
                                               p.name,
                                               p.ring_number,
                                               p.birthdate,
                                               p.condition_status,
                                               p.sex,
                                               p.is_own,
                                               p.country_code,
                                               p.mate_id,
                                               p.father_id,
                                               p.mother_id,
                                               p.keeper_id,
                                               p.section_id,
                                               depth + 1
                                        FROM pigeon p
                                                 JOIN pedigree ped ON ped.father_id = p.id OR ped.mother_id = p.id
                                        )
            SELECT pedigree.id,
                   pedigree.name,
                   ring_number,
                   birthdate,
                   condition_status,
                   sex,
                   is_own,
                   country_code,
                   mate_id,
                   father_id,
                   mother_id,
                   keeper_id,
                   k.name keeper_name,
                   section_id
            FROM pedigree
                      LEFT JOIN keeper k ON keeper_id = k.id AND k.user_id = :userId
            """;
    public static final String PIGEON_WITH_ALL_DESCENDANTS = """
            WITH RECURSIVE pedigree AS (SELECT id,
                                               name,
                                               ring_number,
                                               birthdate,
                                               condition_status,
                                               sex,
                                               is_own,
                                               country_code,
                                               mate_id,
                                               father_id,
                                               mother_id,
                                               keeper_id,
                                               section_id
                                        FROM pigeon
                                        WHERE id = :id
                                          AND pigeon.user_id = :userId
                        
                                        UNION
                        
                                        SELECT p.id,
                                               p.name,
                                               p.ring_number,
                                               p.birthdate,
                                               p.condition_status,
                                               p.sex,
                                               p.is_own,
                                               p.country_code,
                                               p.mate_id,
                                               p.father_id,
                                               p.mother_id,
                                               p.keeper_id,
                                               p.section_id
                                        FROM pigeon p
                                                 JOIN pedigree ped ON p.father_id = ped.id OR p.mother_id = ped.id
                                        )
            SELECT pedigree.id,
                   pedigree.name,
                   ring_number,
                   birthdate,
                   condition_status,
                   sex,
                   is_own,
                   country_code,
                   mate_id,
                   father_id,
                   mother_id,
                   keeper_id,
                   k.name keeper_name,
                   section_id
            FROM pedigree
                     LEFT JOIN keeper k ON keeper_id = k.id AND k.user_id = :userId
            """;
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORITIES = "authorities";
}
