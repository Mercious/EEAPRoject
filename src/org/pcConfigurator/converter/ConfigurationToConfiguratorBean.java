package org.pcConfigurator.converter;

import org.pcConfigurator.beans.ConfiguratorBean;
import org.pcConfigurator.entities.Configuration;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationToConfiguratorBean {

    public ConfiguratorBean convert(final Configuration currentConfiguration) {
        ConfiguratorBean configuratorBean = new ConfiguratorBean();
        return configuratorBean;
    }
}
