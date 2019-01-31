package org.pcConfigurator.services;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.entities.User;
import org.pcConfigurator.managed.RegisterData;

import java.util.Set;

public interface UserService {
    public boolean registerUser(final RegisterData registerData);

    User loginUser(String userName, String password);

    User findUserForUserName(final String userName);
    void saveConfigurationBeanForUserBean(final ConfigurationBean configurationBean, final UserBean userBean);
    Set<User> getAllUsers();
    Set<ConfigurationBean> getSavedConfigurationsForUserBean(final String userName);
}
