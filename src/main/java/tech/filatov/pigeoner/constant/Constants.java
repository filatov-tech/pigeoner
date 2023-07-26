package tech.filatov.pigeoner.constant;

final public class Constants {
    public static final String RING_NUMBER = "ringNumber";
    public static final String PIGEON_NAME = "name";
    public static final String CONDITION = "condition";
    public static final String LOCATION = "location";
    public static final String AGE_YEAR_FROM = "ageYearFrom";
    public static final String AGE_MONTH_FROM = "ageMonthFrom";
    public static final String AGE_YEAR_TO = "ageYearTo";
    public static final String AGE_MONTH_TO = "ageMonthTo";
    public static final String BIRTHDATE_FROM = "birthdateFrom";
    public static final String BIRTHDATE_TO = "birthdateTo";
    public static final String DATE_FILTER_TYPE = "dateFilterType";
    public static final String BIRTHDATE_TYPE = "dateType";
    public static final String AGE_TYPE = "ageType";
    public static final String YEAR_TYPE = "yearType";

    public static final String SECTION_DTO_COMMON_NATIVE_QUERY = "SELECT ms.id AS sect_id,\n" +
            "       ms.name AS sect_name,\n" +
            "       type AS sect_type,\n" +
            "       ms.parent_id AS parent_id,\n" +
            "       (WITH RECURSIVE pigeon_count AS (SELECT v.pigeon_id, v.location_id, v.section_id, v.type, v.parent_id\n" +
            "                                        FROM (SELECT p.id       AS pigeon_id,\n" +
            "                                                     section_id AS location_id,\n" +
            "                                                     s.id       AS section_id,\n" +
            "                                                     type,\n" +
            "                                                     parent_id\n" +
            "                                              FROM pigeon p\n" +
            "                                                       LEFT JOIN section s ON FALSE\n" +
            "                                              UNION\n" +
            "                                              SELECT p.id       AS pigeon_id,\n" +
            "                                                     section_id AS location_id,\n" +
            "                                                     s.id       AS section_id,\n" +
            "                                                     type,\n" +
            "                                                     parent_id\n" +
            "                                              FROM pigeon p\n" +
            "                                                       RIGHT JOIN section s ON FALSE)\n" +
            "                                                 AS v\n" +
            "                                        WHERE v.location_id = ms.id\n" +
            "                                           OR v.parent_id = ms.id\n" +
            "\n" +
            "                                        UNION ALL\n" +
            "\n" +
            "                                        SELECT w.pigeon_id, w.location_id, w.section_id, w.type, w.parent_id\n" +
            "                                        FROM (SELECT p.id       AS pigeon_id,\n" +
            "                                                     section_id AS location_id,\n" +
            "                                                     s.id       AS section_id,\n" +
            "                                                     type,\n" +
            "                                                     parent_id\n" +
            "                                              FROM pigeon p\n" +
            "                                                       LEFT JOIN section s ON FALSE\n" +
            "                                              UNION\n" +
            "                                              SELECT p.id       AS pigeon_id,\n" +
            "                                                     section_id AS location_id,\n" +
            "                                                     s.id       AS section_id,\n" +
            "                                                     type,\n" +
            "                                                     parent_id\n" +
            "                                              FROM pigeon p\n" +
            "                                                       RIGHT JOIN section s ON FALSE)\n" +
            "                                                 AS w\n" +
            "                                                 INNER JOIN pigeon_count\n" +
            "                                                            ON (w.pigeon_id IS NOT NULL AND\n" +
            "                                                                w.location_id = pigeon_count.section_id AND\n" +
            "                                                                pigeon_count.pigeon_id IS NULL)\n" +
            "                                                                OR (w.parent_id = pigeon_count.section_id AND\n" +
            "                                                                    w.pigeon_id IS NULL AND\n" +
            "                                                                    pigeon_count.pigeon_id IS NULL))\n" +
            "        SELECT COUNT(*)\n" +
            "        FROM pigeon_count\n" +
            "        WHERE pigeon_id IS NOT NULL) AS pigeon_number\n" +
            "FROM section ms";
    public static final String SECTION_DTO_ROOT = " WHERE ms.parent_id IS NULL";
    public static final String SECTION_DTO_BY_ID = " WHERE ms.parent_id = :id";
    public static final String SECTION_WITH_DEEP_CHILDREN = "WITH RECURSIVE recursive_sections\n" +
            "                   AS (SELECT s.id, s.parent_id\n" +
            "                       FROM section AS s\n" +
            "                       WHERE s.parent_id = :id\n" +
            "\n" +
            "                       UNION ALL\n" +
            "\n" +
            "                       SELECT sj.id, sj.parent_id\n" +
            "                       FROM section AS sj\n" +
            "                                INNER JOIN recursive_sections AS rs ON rs.id = sj.parent_id)\n" +
            "SELECT rec.id\n" +
            "FROM recursive_sections rec";
    public static final String PIGEON_WITH_3_LEVEL_ANCESTORS = "WITH RECURSIVE pedigree AS (\n" +
            "    SELECT id, ring_number, name, sex, birthdate, condition_status, is_native, father_id, mother_id, keeper_id, 0 AS depth\n" +
            "    FROM pigeon\n" +
            "    WHERE id = :id\n" +
            "\n" +
            "    UNION\n" +
            "\n" +
            "    SELECT p.id, p.ring_number, p.name, p.sex, p.birthdate, p.condition_status, p.is_native, p.father_id, p.mother_id, p.keeper_id, depth+1\n" +
            "    FROM pigeon p\n" +
            "             JOIN pedigree ped ON ped.father_id = p.id OR ped.mother_id = p.id\n" +
            "    WHERE depth < 3\n" +
            ")\n" +
            "SELECT pedigree.id, ring_number, pedigree.name, sex, birthdate, condition_status, is_native, father_id, mother_id, depth, k.name keeper\n" +
            "FROM pedigree\n" +
            "JOIN keeper k ON keeper_id = k.id";
}
