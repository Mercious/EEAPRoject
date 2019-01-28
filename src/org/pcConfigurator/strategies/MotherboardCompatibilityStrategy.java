package org.pcConfigurator.strategies;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfiguratorBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.Motherboard;
import org.pcConfigurator.entities.SlotType;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class MotherboardCompatibilityStrategy implements CompatibilityStrategy {

    @Override
    public boolean isCompatibleToCurrentConfig(Article article, Configuration currentConfig) {
        if (!this.isApplicable(article))
            return true;
        Motherboard motherboard = (Motherboard) article;
        Set<SlotType> totalRequiredSlots = new HashSet<>();
        currentConfig.getConfiguredComponents().stream().filter(component -> component.getRequiredSlot() != null).forEach(component -> {
            totalRequiredSlots.add(component.getRequiredSlot());
        });

        return motherboard.getProvidedSlots().containsAll(totalRequiredSlots);

    }

    @Override
    public boolean isApplicable(Article article) {
        return article instanceof Motherboard;
    }
}
