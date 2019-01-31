package org.pcConfigurator.managed;


import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.converter.ArticleToArticleTeaserBeanConverter;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;
import org.pcConfigurator.entities.SlotRestrictionType;
import org.pcConfigurator.services.ArticleService;
import org.pcConfigurator.services.UserService;

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
import java.util.logging.Logger;

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
    private UserService userService;

    @Inject
    private ArticleToArticleTeaserBeanConverter articleToArticleTeaserBeanConverter;

    @Inject
    private LoginUserManager loginUserManager;

    private List<ArticleTeaserBean> currentItemList;

    private List<ConfigurationBean> savedConfigurations = new ArrayList<>();

    private DecimalFormat priceFormat = new DecimalFormat("##.##");

    // Wird über preRenderEvent geladen statt @PostConstruct, da in der Kombination: User besucht Konfigurator ->
    // User loggt sich ein -> User besucht Konfigurator sonst keine Liste angezeigt wird, da der ConfigurationManager
    // vorher erzeugt wurde
    public void loadSavedConfigurations() {
        if (this.getCurrentUser() != null)
            this.savedConfigurations = new ArrayList<>(
                    this.userService.getSavedConfigurationsForUserBean(getCurrentUser().getUserName()));
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
            ec.redirect(((HttpServletRequest) ec.getRequest()).getContextPath() + "/configurator.xhtml?faces-redirect=true");
        } catch (IOException e) {
            /// error handeling
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
                .filter(article -> article.getType().equals(componentType)).findFirst().orElse(null), false, false);
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

            return;
        }

        this.userService.saveConfigurationBeanForUserBean(currentConfiguration, getCurrentUser());
        // Persistierte Konfigurationen neu laden -> currentConfiguration bleibt ID-los, dient dem Nutzer zum Anlegen neuer Konfigurationen
        // bis er auf eine der gespeicherten wechselt (weil dann nicht mehr ID-los)
        this.loadSavedConfigurations();

    }

    public UserBean getCurrentUser() {
        return this.loginUserManager.getCurrentUser();
    }
}
