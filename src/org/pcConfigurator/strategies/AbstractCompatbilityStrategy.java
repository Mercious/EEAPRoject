package org.pcConfigurator.strategies;

import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.SlotRestriction;
import org.pcConfigurator.entities.SlotRestrictionType;
import org.pcConfigurator.entities.SlotType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCompatbilityStrategy {

    boolean compareSlotTypeMapForCoverage(HashMap<SlotType, Integer> requiredSlots, HashMap<SlotType, Integer> providedSlots) {
        for (SlotType slotType : requiredSlots.keySet()) {
            if (providedSlots.get(slotType) == null || providedSlots.get(slotType) < requiredSlots.get(slotType))
                return false;
        }

        return true;
    }

    HashMap<SlotType, Integer> mergeSlotTypeMaps(final HashMap<SlotType, Integer> first, final HashMap<SlotType, Integer> second) {
        HashMap<SlotType, Integer> mergedMap = new HashMap<>(first);

        for (SlotType slotType : second.keySet()) {
            mergedMap.merge(slotType, second.get(slotType), (a, b) -> a + b);
        }
        return mergedMap;
    }

    HashMap<SlotType, Integer> removeSlotTypeMapFromOther(final HashMap<SlotType, Integer> slotTypeMap, final HashMap<SlotType, Integer> other) {
        HashMap<SlotType, Integer> mergedMap = new HashMap<>(other);
        for (SlotType slotType : slotTypeMap.keySet()) {
            if (mergedMap.get(slotType) == null || mergedMap.get(slotType) <= slotTypeMap.get(slotType))
                mergedMap.remove(slotType);
            else
                mergedMap.merge(slotType, slotTypeMap.get(slotType), (a,b) -> a - b);
        }
        return mergedMap;
    }



}
