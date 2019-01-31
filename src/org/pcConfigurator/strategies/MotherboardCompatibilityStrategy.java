package org.pcConfigurator.strategies;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;
import org.pcConfigurator.entities.SlotRestrictionType;
import org.pcConfigurator.entities.SlotType;

import javax.ejb.Stateless;
import java.util.HashMap;

@Stateless
public class MotherboardCompatibilityStrategy extends AbstractCompatbilityStrategy implements CompatibilityStrategy {

    @Override
    public boolean isCompatibleToCurrentConfig(Article article, ConfigurationBean currentConfig) {
        if (!this.isApplicable(article))
            return true;
        // Die Konfiguration hat bereits ein Motherboard
        if (currentConfig.getConfiguredComponents().stream()
                .filter(configuredComponent -> configuredComponent.getType().equals(article.getType()))
                .findFirst().orElse(null) != null)
            return false;


        HashMap<SlotType, Integer> providedSlotsWithMB =
                mergeSlotTypeMaps(article.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES), currentConfig.getTotalProvidedSlots());

        for (Article configuredComponent : currentConfig.getConfiguredComponents()) {
            HashMap<SlotType, Integer> componentRequiredSlots = configuredComponent.getSlotRestrictionsOfType(SlotRestrictionType.REQUIRES);
            // Um zu verhindern, dass Komponenten für sich selber Slots providen, rechnen wir aus der gesamten Map aus provideten
            // Slots die provideten Slots der betrachteten Komponente wieder raus
            HashMap<SlotType, Integer> totalProvidedSlotsWithoutComponent = removeSlotTypeMapFromOther
                    (configuredComponent.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES), providedSlotsWithMB);
            if (!compareSlotTypeMapForCoverage(componentRequiredSlots, totalProvidedSlotsWithoutComponent))
                return false;
            else
                // Nun müssen wir natürlich für die weitere Betrachtung die gerade "verwendeten" Slots aus den zur Verfügung
                // stehenden rausrechnen
                providedSlotsWithMB = removeSlotTypeMapFromOther(componentRequiredSlots, providedSlotsWithMB);
        }

        // Wenn wir durch alle Artikel geloopt sind, ohne jemals den Fall zu haben, dass die benötigten Slots eines Artikels
        // nicht in der restlichen Menge an verfügbaren Slots enthalten ist, dann sollten wir an der Stelle eine passende
        // Konfiguration haben.
        return true;

    }

    @Override
    public boolean isApplicable(Article article) {
        return ComponentType.MB.equals(article.getType());
    }
}
