package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.PigeonDto;

import java.util.*;

public class PigeonUtil {
    private PigeonUtil() {}

    public static PigeonDto buildPedigree(List<PigeonDto> pigeons) {

        Map<Long, PigeonDto> pigeonsMap = new HashMap<>();
        Deque<PigeonDto> pigeonStack = new ArrayDeque<>(9);
        PigeonDto rootPigeon = null;


        for (PigeonDto pigeon : pigeons) {
            pigeonsMap.put(pigeon.getId(), pigeon);
            if (pigeon.getDepth() == 0) {
                pigeonStack.addFirst(pigeon);
                rootPigeon = pigeon;
            }
        }

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
