package org.pcConfigurator.strategies;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.*;

import javax.ejb.Stateless;
import java.util.Set;

@Stateless
public class ComponentCompatibilityStrategy extends AbstractCompatbilityStrategy implements CompatibilityStrategy {
    @Override
    public boolean isCompatibleToCurrentConfig(final Article article, final ConfigurationBean currentConfig) {
        if (!this.isApplicable(article))
            return true;
        if (article.getSlotRestrictions().isEmpty())
            return true;

        for (Article containedArticle : currentConfig.getConfiguredComponents()) {
            if (ComponentType.MB.equals(containedArticle.getType())) {
                Set<SlotType> supportedSlots = getSlotTypesOfRestrictionTypeForArticle(containedArticle, SlotRestrictionType.PROVIDES);
                return supportedSlots.containsAll(getSlotTypesOfRestrictionTypeForArticle(article, SlotRestrictionType.REQUIRES));
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
