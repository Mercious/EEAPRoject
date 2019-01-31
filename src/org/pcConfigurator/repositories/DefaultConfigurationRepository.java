package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class DefaultConfigurationRepository implements ConfigurationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Configuration> findByUser(final User creator) {
        TypedQuery<Configuration> query = this.entityManager.createQuery
                ("select c from Configuration c where c.creator = :creator", Configuration.class);
        query.setParameter("creator", creator);

        return new HashSet<>(query.getResultList());
    }

    @Override
    public void save(Configuration configuration) {
        if (configuration.getId() == null)
            this.entityManager.persist(configuration);
        else
            this.entityManager.merge(configuration);
    }

    @Override
    public Configuration findById(long id) {
        return this.entityManager.find(Configuration.class, id);
    }
}
