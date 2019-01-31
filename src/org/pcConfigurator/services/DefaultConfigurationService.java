package org.pcConfigurator.services;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.converter.ConfigurationToConfigurationBeanConverter;
import org.pcConfigurator.repositories.ConfigurationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

@Stateless
public class DefaultConfigurationService implements ConfigurationService, Serializable {

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private ConfigurationToConfigurationBeanConverter configurationToConfigurationBeanConverter;

    @Inject
    private UserService userService;

    @Override
    public ConfigurationBean getConfigurationBeanForId(Long configurationId) {
        return this.configurationToConfigurationBeanConverter.convert(this.configurationRepository.findById(configurationId));
    }


}
