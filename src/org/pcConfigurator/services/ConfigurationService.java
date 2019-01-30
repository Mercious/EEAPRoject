package org.pcConfigurator.services;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.entities.User;

import java.util.Set;

public interface ConfigurationService {

    public Set<ConfigurationBean> getSavedConfigurationBeansForUser(final UserBean creator);
    public void saveConfigurationBean(final ConfigurationBean configurationBean);
    public ConfigurationBean getConfigurationBeanForId(final long configurationId);
}
