package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.Collection;
import java.util.List;

public class PigeonUtil {
    private PigeonUtil() {}

    private static final String BUTTON_NAME = "Подробнее ";
    private static final String BUTTON_LINK = "button=/pigeons/";

    public static List<PigeonTableDto> getDtos(Collection<Pigeon> pigeons) {
        return pigeons.stream().map(PigeonUtil::createDto).toList();
    }

    private static PigeonTableDto createDto(Pigeon pigeon) {
        return new PigeonTableDto(
                pigeon.getId(),
                pigeon.getRingNumber(),
                pigeon.getColor().getName(),
                pigeon.isMale() ? "М" : "Ж",
                pigeon.getBirthdate(),
                TimeUtil.getAgeFromBirthday(pigeon.getBirthdate()),
                "nullstub",
                pigeon.getConditionStatus().label
        );
    }

    public static String getPigeonsInString(Collection<PigeonTableDto> pigeons) {
        StringBuilder result = new StringBuilder();
        for (PigeonTableDto pigeon : pigeons) {
            result
                    .append(pigeon.toOneRow())
                    .append(BUTTON_NAME + BUTTON_LINK)
                    .append(pigeon.getId())
                    .append("\n");
        }
        return result.isEmpty() ? result.toString() : result.substring(0, result.length() - 2);
    }


}
