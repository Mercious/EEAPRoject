package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.User;

import java.util.Set;

public interface UserRepository {
    public Set<User> findAll();

    public User findOne(final long userId);

    public User findByUserName(final String userName);

    public void save(final User user);

}
