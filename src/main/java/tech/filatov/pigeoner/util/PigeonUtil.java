package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.PigeonDto;

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
}
