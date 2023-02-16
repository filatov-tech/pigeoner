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
