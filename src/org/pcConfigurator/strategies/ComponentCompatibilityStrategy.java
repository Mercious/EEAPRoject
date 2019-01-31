package org.pcConfigurator.strategies;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.*;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Set;

@Stateless
public class ComponentCompatibilityStrategy extends AbstractCompatbilityStrategy implements CompatibilityStrategy {
    @Override
    public boolean isCompatibleToCurrentConfig(final Article article, final ConfigurationBean currentConfig) {
        if (!this.isApplicable(article))
            return true;
        if (article.getSlotRestrictions().isEmpty())
            return true;

        // Die Konfiguration hat bereits einen Artikel dieses Typen -> Nur für Peripherals erlaubt, ansonsten nicht, da Slot belegt
        if (!ComponentType.PERIPHERAL.equals(article.getType()) && currentConfig.getConfiguredComponents().stream().filter(
                configuredComponent -> configuredComponent.getType().equals(article.getType())).findFirst().orElse(null)
                != null)
            return false;

        for (Article containedArticle : currentConfig.getConfiguredComponents()) {
            if (ComponentType.MB.equals(containedArticle.getType())) {
               HashMap<SlotType, Integer> componentRequiredSlots = article.getSlotRestrictionsOfType(SlotRestrictionType.REQUIRES);
               // We check that if we add this component, whether or not the totally provided slots are still enough
                // after we added this components requirements
               return compareSlotTypeMapForCoverage(mergeSlotTypeMaps(currentConfig.getTotalRequiredSots(),
                       componentRequiredSlots), currentConfig.getTotalProvidedSlots());
            }
        }
        // kein Motherboard gefunden -> Artikel ist erstmal kompatibel, Entscheidung fällt dann beim Motherboard
        return true;
    }

    @Override
    public boolean isApplicable(final Article article) {
        ComponentType articleType = article.getType();
        // Wir schreiben hier explizit alle hin anstatt auf (!ComponentType.MB) zu prüfen, da in Zukunft eventuell weitere
        // ComponentType's hinzukommen könnten, die diese Logik dann sehr feheranfällig machen würde
        return ComponentType.CPU.equals(articleType) || ComponentType.GPU.equals(articleType) || ComponentType.RAM.equals(articleType)
                || ComponentType.HDD.equals(articleType) || ComponentType.PERIPHERAL.equals(articleType)
                || ComponentType.PSU.equals(articleType);
    }


}
