package org.pcConfigurator.ws;

import org.pcConfigurator.services.UserService;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class CustomerDataWebservice {
    @Inject
    private UserService userService;

    @WebMethod
    public String getUserDataForNameJSON(final String userName) {
        return "";
    }
}
