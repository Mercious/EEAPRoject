package org.pcConfigurator.managed;


import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.converter.ArticleToArticleTeaserBeanConverter;
import org.pcConfigurator.converter.ConfigurationToConfiguratorBean;
import org.pcConfigurator.entities.*;
import org.pcConfigurator.services.ArticleService;
import org.pcConfigurator.services.ConfigurationService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    private ConfigurationService configurationService;

    @Inject
    private ConfigurationToConfiguratorBean configurationToConfiguratorBean;

    @Inject
    private ArticleToArticleTeaserBeanConverter articleToArticleTeaserBeanConverter;

    @Inject
    private LoginUserManager loginUserManager;

    private List<ArticleTeaserBean> currentItemList;

    private List<ConfigurationBean> savedConfigurations = new ArrayList<>();

    private DecimalFormat priceFormat = new DecimalFormat("##.00");


    // TODO : Fehlerhaftes verhalten bei dem Szenario: User besucht Konfigrator -> User logt sich ein -> user besucht wieder Konfig
    // Liste an persistierten Konfigurationen bleibt leer, weil sie hier bereits vorher initilisiert wurde (als es noch keinen User gab)
    @PostConstruct
    public void loadSavedConfigurations() {
        if (this.getCurrentUser() != null)
            this.savedConfigurations = new ArrayList<>(this.configurationService.getSavedConfigurationBeansForUser(this.getCurrentUser()));
    }




    public ConfigurationBean getCurrentConfiguration() {
        return currentConfiguration;
    }

    public void getCompatibleArticlesOfType(final String componentType) {
        final ComponentType type = ComponentType.valueOf(componentType);
        // JSF's repeat-tags do not support set's
        this.currentItemList = new ArrayList<>(articleService.getCompatibleArticleTeaserBeansOfType(type, currentConfiguration));
    }

    public void addArticleToConfiguration(final ArticleTeaserBean toAddTeaserBean) {
        // Keine weitere Überprüfung auf Kompatibilität hier. Sollte ein User mit händisch erzeugten Requests
        // Konfigurationen zusammenfügen, bei denen Komponenten nicht miteinander kompatibel sind, dann ist mir das
        // herzlich egal.
        if (toAddTeaserBean == null)
            return;
        final Article toAddComponent = this.articleService.getArticleForArticleTeaserBean(toAddTeaserBean);
        currentConfiguration.addComponent(toAddComponent);
        currentConfiguration.addProvidedSlots(toAddComponent.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES));
        currentConfiguration.addRequiredSlots(toAddComponent.getSlotRestrictionsOfType(SlotRestrictionType.REQUIRES));
        // Item-Liste wieder ausblenden
        currentItemList = null;
        DecimalFormat df = new DecimalFormat("#.00");

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI() + "?faces-redirect=true");
        } catch (IOException e) {
            // error handeling
        }


    }

    public void removeArticleFromConfiguration(final ArticleTeaserBean toRemoveTeaserBean) {
        if (toRemoveTeaserBean == null)
            return;
        final Article toRemoveComponent = this.articleService.getArticleForArticleTeaserBean(toRemoveTeaserBean);
        currentConfiguration.removeComponent(toRemoveComponent);
        currentConfiguration.removeRequiredSlots(toRemoveComponent.getSlotRestrictionsOfType(SlotRestrictionType.REQUIRES));
        currentConfiguration.removeProvidedSlots(toRemoveComponent.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES));
        DecimalFormat df = new DecimalFormat("#.00");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI() + "?faces-redirect=true");
        } catch (IOException e) {
            // error handeling
        }
    }

    public ConfigurationBean getCurrentConfigurationBean() {
        return currentConfiguration;
    }

    public void clearCurrentConfiguration() {
        this.currentConfiguration = new ConfigurationBean();
    }

    public void setCurrentConfiguration(ConfigurationBean currentConfiguration) {
        this.currentConfiguration = currentConfiguration;
    }

    public ArticleTeaserBean getArticleInSlot(final String slotName) {
        ComponentType componentType = ComponentType.valueOf(slotName);
        return articleToArticleTeaserBeanConverter.convertConsideringCompatibility(this.currentConfiguration.getConfiguredComponents().stream()
                .filter(article -> article.getType().equals(componentType)).findFirst().orElse(null), false);
    }

    public List<ArticleTeaserBean> getCurrentItemList() {
        return currentItemList;
    }

    public String getTotalPrice() {
        return this.priceFormat.format(this.currentConfiguration.getTotalPrice());
    }

    public List<ConfigurationBean> getSavedConfigurations() {
        return savedConfigurations;
    }

    public void saveConfiguration() {
        if (currentConfiguration.getConfiguredComponents() == null || currentConfiguration.getConfiguredComponents().isEmpty()
                || getCurrentUser() == null) {
            // Button sollte gar nicht enabled sein -> war manuelle Post-Request / Eingriff, benötigt keine Information
            // (Button ist erst dann enabled, wenn die Konfiguration mindestens einen Eintrag hat und wenn es einen eingeloggten
            // User gibt)
            return;
        }
        this.currentConfiguration.setCreator(getCurrentUser());
        this.configurationService.saveConfigurationBean(currentConfiguration);
        this.loadSavedConfigurations();

    }

    public UserBean getCurrentUser() {
        return this.loginUserManager.getCurrentUser();
    }
}
