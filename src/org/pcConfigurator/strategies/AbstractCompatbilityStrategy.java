package org.pcConfigurator.strategies;

import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.SlotRestriction;
import org.pcConfigurator.entities.SlotRestrictionType;
import org.pcConfigurator.entities.SlotType;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCompatbilityStrategy {

     Set<SlotType> getSlotTypesOfRestrictionTypeForArticle(final Article article, final SlotRestrictionType type) {
        return article.getSlotRestrictions().stream()
                .filter(slotRestriction -> type.equals(slotRestriction.getType())).map(SlotRestriction::getRelatedSlotType)
                .collect(Collectors.toSet());

    }


}
