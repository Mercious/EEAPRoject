package org.pcConfigurator.converter;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.SlotRestrictionType;
import org.pcConfigurator.services.UserService;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;

@ApplicationScoped
public class ConfigurationToConfigurationBeanConverter {
    @Inject
    private UserService userService;
    public ConfigurationBean convert(final Configuration source) {
        ConfigurationBean configurationBean = new ConfigurationBean();
        configurationBean.setConfiguredComponents(new ArrayList<>(source.getConfiguredComponents()));
        configurationBean.getConfiguredComponents().forEach(article -> {
            configurationBean.addRequiredSlots(article.getSlotRestrictionsOfType(SlotRestrictionType.REQUIRES));
            configurationBean.addProvidedSlots(article.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES));
        });
        configurationBean.setCreator(new UserBean(source.getCreator().getUserName(),
                source.getCreator().getFirstName(), source.getCreator().getLastName()));
        configurationBean.setConfigurationId(source.getId());

        return configurationBean;
    }

    public Configuration from(final ConfigurationBean source) {
        Configuration target = new Configuration();
        target.setConfiguredComponents(new HashSet<>(source.getConfiguredComponents()));
        target.setCreator(this.userService.findUserForUserBean(source.getCreator()));
        return target;
    }
}
