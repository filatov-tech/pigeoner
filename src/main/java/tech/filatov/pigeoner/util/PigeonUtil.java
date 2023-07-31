package tech.filatov.pigeoner.util;

import tech.filatov.pigeoner.dto.PigeonWithAncestorsDto;

import java.util.*;

public class PigeonUtil {
    private PigeonUtil() {}

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
