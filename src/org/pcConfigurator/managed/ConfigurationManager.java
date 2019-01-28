package org.pcConfigurator.managed;


import org.pcConfigurator.beans.ConfiguratorBean;
import org.pcConfigurator.converter.ConfigurationToConfiguratorBean;
import org.pcConfigurator.entities.*;
import org.pcConfigurator.services.ArticleService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SessionScoped
@Named
public class ConfigurationManager implements Serializable {
    private Configuration currentConfiguration = new Configuration();

    @Inject
    private ArticleService articleService;

    @Inject
    private ConfigurationToConfiguratorBean configurationToConfiguratorBean;

    public Configuration getCurrentConfiguration() {
        return currentConfiguration;
    }

    public Set<Article> getCompatibleArticlesOfType(ComponentType componentType) {
        return articleService.getCompatibleArticlesOfType(componentType, currentConfiguration);
    }

    public void addArticleToConfiguration(final Component toAddComponent) {
        // Keine weitere Überprüfung auf Kompatibilität hier. Sollte ein User mit händisch erzeugten Requests
        // Konfigurationen zusammenfügen, bei denen Komponenten nicht miteinander kompatibel sind, dann ist mir das
        // herzlich egal.
        Set<Component> currentComponents = currentConfiguration.getConfiguredComponents();
        currentComponents.forEach(component -> {
            if (component.getType().equals(toAddComponent.getType()))
                currentComponents.remove(component);
        });
        currentComponents.add(toAddComponent);
    }

    public ConfiguratorBean getCurrentConfigurationBean() {
        return this.configurationToConfiguratorBean.convert(currentConfiguration);
    }

    public void addArticleToConfiguration(final Motherboard toAddMotherboard) {

    }

    public void setCurrentConfiguration(Configuration currentConfiguration) {
        this.currentConfiguration = currentConfiguration;
    }
}
