package org.pcConfigurator.services;

import org.pcConfigurator.entities.User;
import org.pcConfigurator.managed.RegisterData;

public interface UserService {
    public boolean registerUser(final RegisterData registerData);

    User loginUser(String userName, String password);
}
