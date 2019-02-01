package org.pcConfigurator.converter;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.SlotRestrictionType;

import javax.faces.bean.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashSet;

@ApplicationScoped
public class ConfigurationToConfigurationBeanConverter {


    public ConfigurationBean convert(final Configuration source) {
        ConfigurationBean configurationBean = new ConfigurationBean();
        configurationBean.setConfiguredComponents(new ArrayList<>(source.getConfiguredComponents()));
        configurationBean.getConfiguredComponents().forEach(article -> {
            configurationBean.addRequiredSlots(article.getSlotRestrictionsOfType(SlotRestrictionType.REQUIRES));
            configurationBean.addProvidedSlots(article.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES));
        });
        configurationBean.setConfigurationId(source.getId());

        return configurationBean;
    }

    public Configuration from(final ConfigurationBean source) {
        Configuration target = new Configuration();
        target.setConfiguredComponents(new HashSet<>(source.getConfiguredComponents()));
        target.setId(source.getConfigurationId());
        return target;
    }
}
