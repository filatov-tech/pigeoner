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
        pigeon.setId(dto.getId());
        pigeon.setRingNumber(dto.getRingNumber());
        pigeon.setName(dto.getName());
        pigeon.setBirthdate(dto.getBirthdate());
        pigeon.setConditionStatus(Condition.valueOf(dto.getCondition()));
        pigeon.setSex(Sex.valueOf(dto.getSex()));
        return pigeon;
    }

}
