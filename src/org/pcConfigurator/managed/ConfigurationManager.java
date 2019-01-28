package org.pcConfigurator.managed;


import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.converter.ConfigurationToConfiguratorBean;
import org.pcConfigurator.entities.*;
import org.pcConfigurator.services.ArticleService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

/*
    Hier soll die Logik des Konfigurierens und damit auch was der User als Option im Frontend angezeigt bekommt stattfinden.
    Die Logik, auf die ich mich dabei festgelegt habe, ist in etwa die, die ich als Nutzer im Frontend auch erwartwen würde.
    Demnach kann der User nach und nach Komponenten seiner Wahl hinzufügen. Sobald es sich bei deiner der Komponenten um ein
    Motherboard handelt, wird überprüft, ob dieses Motherboard den bisherigen Anspruch an Slots durch die bereits konfigurierten
    Komponenten abdecken kann.
    Beim Hinzufügen einer nicht-Motherboard Komponente wird die gleiche Logik überprüft, sofern bereits ein Motherboard hinzuefügt wurde.
    Das erlaubt es dem Nutzer anfangs z.B. erst einen CPU auszuwählen und dann ein passendes Motherboard, anstatt die umgekehrte Logik
    erwzingen zu müssen.
 */
@SessionScoped
@Named
public class ConfigurationManager implements Serializable {
    private ConfigurationBean currentConfiguration = new ConfigurationBean();

    @Inject
    private ArticleService articleService;

    @Inject
    private ConfigurationToConfiguratorBean configurationToConfiguratorBean;

    public ConfigurationBean getCurrentConfiguration() {
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

    public ConfigurationBean getCurrentConfigurationBean() {
        return this.configurationToConfiguratorBean.convert(currentConfiguration);
    }

    public void addArticleToConfiguration(final Motherboard toAddMotherboard) {

    }

    public void setCurrentConfiguration(Configuration currentConfiguration) {
        this.currentConfiguration = currentConfiguration;
    }
}
