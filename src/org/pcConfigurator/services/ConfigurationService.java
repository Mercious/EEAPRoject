package org.pcConfigurator.services;

import org.pcConfigurator.beans.ConfigurationBean;

public interface ConfigurationService {
    ConfigurationBean getConfigurationBeanForId(final Long configurationId);
}
