package org.pcConfigurator.converter;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Configuration;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationToConfiguratorBean {

    public ConfigurationBean convert(final Configuration currentConfiguration) {
        ConfigurationBean configuratorBean = new ConfigurationBean();
        return configuratorBean;
    }
}
