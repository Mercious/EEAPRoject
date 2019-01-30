package org.pcConfigurator.converter;

import org.pcConfigurator.entities.User;
import org.pcConfigurator.managed.RegisterData;

import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
public class RegisterDataToUserConverter implements Serializable {

    public User convert(final RegisterData source) {
        User user = new User();
        user.setUserName(source.getUserName());
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.seteMail(source.getEmail());
        user.setCity(source.getCity());
        user.setStreetNumber(source.getStreetNumber());
        user.setStreetName(source.getStreet());
        // Erstmal hardcoded, da die Anwendung nur für Deutschland entwickelt ist
        user.setCountry("Deutschland");
        user.setPostalCode(source.getZipcode());
        user.setPassword(source.getPassword());
        return user;
    }
}
