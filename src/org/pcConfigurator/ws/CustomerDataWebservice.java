package org.pcConfigurator.ws;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.converter.ConfigurationToConfigurationBeanConverter;
import org.pcConfigurator.entities.Configuration;
import org.pcConfigurator.entities.User;
import org.pcConfigurator.services.UserService;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Set;

@WebService
public class CustomerDataWebservice {
    @Inject
    private UserService userService;
    @Inject
    private ConfigurationToConfigurationBeanConverter configurationToConfigurationBeanConverter;

    @WebMethod
    public User getUserDataForName(final String userName) {
       return this.userService.findUserForUserName(userName);
    }

    @WebMethod
    public Set<ConfigurationBean> getUserConfigurationsForUserName(final String userName) {
        return this.userService.getSavedConfigurationsForUserBean(userName);
    }

    @WebMethod
    public Set<User> getAllUserData() {
        return this.userService.getAllUsers();
    }
}
