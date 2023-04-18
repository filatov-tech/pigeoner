package tech.filatov.pigeoner.constant;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    private static final Map<String, String> FIELDS_MAPPING = new HashMap<>();
    public static final String SEP = ";";
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
    public static final String FILTER_DATE_TYPE = "filterDateType";
    public static final String BIRTHDATE_TYPE = "birthdate";
    public static final String AGE_TYPE = "age";
    public static final String SECTION_DTO_COMMON_NATIVE_QUERY = "SELECT ms.id AS sect_id,\n" +
            "       ms.name AS sect_name,\n" +
            "       type AS sect_type,\n" +
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
            "FROM section ms\n" +
            "\n" +
            "WHERE ms.parent_id";
    public static final String SECTION_DTO_ROOT = " IS NULL";
    public static final String SECTION_DTO_BY_ID = " = :id";


    static {
        FIELDS_MAPPING.put("ringNumber", "Кольцо");
        FIELDS_MAPPING.put("color", "Окрас");
        FIELDS_MAPPING.put("sex", "Пол");
        FIELDS_MAPPING.put("birthday", "Родился");
        FIELDS_MAPPING.put("age", "Возраст");
        FIELDS_MAPPING.put("mate", "Пара");
        FIELDS_MAPPING.put("status", "Состояние");
    }

    public static String getUiName(String fieldName) {
        return FIELDS_MAPPING.get(fieldName);
    }
}
