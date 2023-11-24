package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.PigeonDto;
import tech.filatov.pigeoner.dto.PigeonShallowDto;
import tech.filatov.pigeoner.model.pigeon.Condition;
import tech.filatov.pigeoner.model.pigeon.Pigeon;
import tech.filatov.pigeoner.model.pigeon.Sex;

import java.util.*;

public class PigeonUtil {
    private PigeonUtil() {}

    public static PigeonDto buildPedigree(List<PigeonDto> pigeons, long rootPigeonId) {

        Map<Long, PigeonDto> pigeonsMap = CommonUtil.getLookupMapFrom(pigeons);
        Deque<PigeonDto> pigeonStack = new ArrayDeque<>(9);

        PigeonDto rootPigeon = pigeonsMap.get(rootPigeonId);
        pigeonStack.addFirst(rootPigeon);

        while (!pigeonStack.isEmpty()) {
            PigeonDto processedPigeon = pigeonStack.pop();
            PigeonDto father = pigeonsMap.get(processedPigeon.getFatherId());
            PigeonDto mother = pigeonsMap.get(processedPigeon.getMotherId());
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

    public static Pigeon getPigeonFrom(PigeonShallowDto dto) {
        Pigeon pigeon = new Pigeon();
        return setDataTo(pigeon, dto);
    }

    public static Pigeon fillWithUpdatedFields(Pigeon pigeon, PigeonShallowDto dto) {
        return setDataTo(pigeon, dto);
    }

    private static Pigeon setDataTo(Pigeon pigeon, PigeonShallowDto dto) {
        pigeon.setId(dto.getId());
        pigeon.setRingNumber(dto.getRingNumber());
        pigeon.setName(dto.getName());
        pigeon.setBirthdate(dto.getBirthdate());
        pigeon.setConditionStatus(dto.getCondition() == null ? null : Condition.valueOf(dto.getCondition()));
        pigeon.setSex(dto.getSex() == null ? null : Sex.valueOf(dto.getSex()));
        pigeon.setCountryCode(dto.getCountryCode());
        return pigeon;
    }
}
