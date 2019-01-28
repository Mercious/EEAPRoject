package org.pcConfigurator.strategies;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfiguratorBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.Component;
import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.Motherboard;

import javax.ejb.Stateless;

@Stateless
public class ComponentCompatibilityStrategy implements CompatibilityStrategy {
    @Override
    public boolean isCompatibleToCurrentConfig(Article article, Configuration currentConfig) {
        if(!this.isApplicable(article))
            return true;
        Component component = (Component) article;
        Motherboard currentMotherboard = currentConfig.getMotherboard();
        return currentMotherboard != null && currentMotherboard.getProvidedSlots().contains(component.getRequiredSlot());
    }

    @Override
    public boolean isApplicable(Article article) {
        return article instanceof Component;
    }


}
