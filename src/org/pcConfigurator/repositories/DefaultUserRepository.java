package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DefaultUserRepository implements UserRepository, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<User> findAll() {
        return new HashSet<>(this.entityManager.createQuery("select U from User u", User.class).getResultList());
    }

    @Override
    public User findOne(long userId) {
        return null;
    }

    @Override
    public User findByUserName(String userName) {
        TypedQuery<User> query = this.entityManager.createQuery("select u from User u where u.userName = :userName", User.class);
        query.setParameter("userName", userName);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            // error handeling
            return null;
        }
    }

    @Override
    public void save(final User user) {
        if (user.getId() != null)
            this.entityManager.merge(user);
        else
            this.entityManager.persist(user);
    }
}
