package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.PigeonTableDto;
import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;
import tech.filatov.pigeoner.model.pigeon.Pigeon;

import java.util.*;

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

    public static PigeonWithAncestorsDto buildPedigree(List<PigeonWithAncestorsDto> pigeons) {

        Map<Long, PigeonWithAncestorsDto> pigeonsMap = new HashMap<>();
        Deque<PigeonWithAncestorsDto> pigeonStack = new ArrayDeque<>(9);
        PigeonWithAncestorsDto rootPigeon = null;


        for (PigeonWithAncestorsDto pigeon : pigeons) {
            pigeonsMap.put(pigeon.getId(), pigeon);
            if (pigeon.getDepth() == 0) {
                pigeonStack.addFirst(pigeon);
                rootPigeon = pigeon;
            }
        }

        while (!pigeonStack.isEmpty()) {
            PigeonWithAncestorsDto processedPigeon = pigeonStack.pop();
            PigeonWithAncestorsDto father = pigeonsMap.get(processedPigeon.getFatherId());
            PigeonWithAncestorsDto mother = pigeonsMap.get(processedPigeon.getMotherId());
            if (father != null) {
                processedPigeon.setFather(father);
                pigeonStack.addFirst(father);
            }
            if (mother != null) {
                processedPigeon.setMother(mother);
                pigeonStack.addLast(mother);
            }
        }
        return rootPigeon;
    }
}
