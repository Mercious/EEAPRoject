package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Set;

public class DefaultUserRepository implements UserRepository, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<User> findAll() {
        return null;
    }

    @Override
    public User findOne(long userId) {
        return null;
    }

    @Override
    public User findByUserName(String userName) {
        return null;
    }

    @Override
    public void save(final User user) {
        this.entityManager.persist(user);
    }
}
