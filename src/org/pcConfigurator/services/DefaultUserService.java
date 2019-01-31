package org.pcConfigurator.services;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.converter.ConfigurationToConfigurationBeanConverter;
import org.pcConfigurator.converter.RegisterDataToUserConverter;
import org.pcConfigurator.entities.User;
import org.pcConfigurator.managed.RegisterData;
import org.pcConfigurator.repositories.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class DefaultUserService implements UserService, Serializable {

    @Inject
    private UserRepository userRepository;
    @Inject
    private RegisterDataToUserConverter registerDataToUserConverter;
    @Inject
    private ConfigurationToConfigurationBeanConverter configurationToConfigurationBeanConverter;

    @Override
    public boolean registerUser(RegisterData registerData) {
        // User Name ist schon vergeben -> return false
        if (this.userRepository.findByUserName(registerData.getUserName()) != null)
            return false;
        // TODO: Hash password before passing registerData to converter
        this.userRepository.save(registerDataToUserConverter.convert(registerData));
        // Sollte hier eine Exception fliegen, wüsste ich eh nicht wie ich es an der Stelle programmatisch lösen sollte
        // Der einzig fachliche Fall, der des bereits existierenden User-Namens, ist abgedeckt, jeder andere Fehler
        // müsste also sowieso händisch und vermtl mit Codeänderungen verbunden angepasst werden -> kein exception handeling hier
        return true;
    }

    @Override
    public User loginUser(final String userName, final String password) {
        User user = this.userRepository.findByUserName(userName);
        // TODO - Hash password parameter before comparing
        if (user != null && password.equals(user.getPassword()))
            return user;
        return null;

    }

    @Override
    public User findUserForUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    @Override
    public void saveConfigurationBeanForUserBean(ConfigurationBean configurationBean, UserBean userBean) {
        User user = this.userRepository.findByUserName(userBean.getUserName());
        if (user == null) {
            // TODO: Logging
            return;
        }
        user.addConfiguration(configurationToConfigurationBeanConverter.from(configurationBean));
        this.userRepository.save(user);
    }

    @Override
    public Set<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public Set<ConfigurationBean> getSavedConfigurationsForUserBean(final String userName) {
        User user = this.userRepository.findByUserName(userName);
        if (user == null)
            return null;
        return user.getSavedConfigurations().stream()
                .map(configuration -> this.configurationToConfigurationBeanConverter.convert(configuration))
                .collect(Collectors.toSet());
    }
}
