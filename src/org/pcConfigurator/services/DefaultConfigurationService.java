package org.pcConfigurator.services;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.converter.ConfigurationToConfigurationBeanConverter;
import org.pcConfigurator.entities.User;
import org.pcConfigurator.repositories.ConfigurationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class DefaultConfigurationService implements ConfigurationService, Serializable {

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private ConfigurationToConfigurationBeanConverter configurationToConfigurationBeanConverter;

    @Inject
    private UserService userService;

    @Override
    public Set<ConfigurationBean> getSavedConfigurationBeansForUser(UserBean creator) {
        return this.configurationRepository.findByUser(userService.findUserForUserBean(creator)).stream()
                .map(configuration -> this.configurationToConfigurationBeanConverter.convert(configuration))
                .collect(Collectors.toSet());
    }

    @Override
    public void saveConfigurationBean(ConfigurationBean configurationBean) {
        this.configurationRepository.save(this.configurationToConfigurationBeanConverter.from(configurationBean));
    }

    @Override
    public ConfigurationBean getConfigurationBeanForId(long configurationId) {
        return this.configurationToConfigurationBeanConverter.convert(this.configurationRepository.findById(configurationId));
    }


}
