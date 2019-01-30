package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.User;

import java.util.Set;

public interface ConfigurationRepository {
    Set<Configuration> findByUser(final User creator);
    void save(final Configuration configuration);
    Configuration findById(final long id);
}
