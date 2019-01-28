package org.pcConfigurator.managed;


import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.services.UserService;
import org.pcConfigurator.util.FacesMessageUtil;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named
public class RegisterUserManager {

    @Inject
    private LoginUserManager loginUserManager;
    @Inject
    private RegisterData registerData;
    @Inject
    private UserService userService;

    public String registerUser() {
        // JSF unterstüzt nicht den Vergleich zweier Input-Felder als Validierung -> wir machen es hier einfach händisch
        // für passwort mit passwortRepeat
        // wir sind hier im jsf-lifecycle post-validierung, also müssen wir nicht auf null prüfen für @NotNull annotierte Felder
        if (!registerData.getPassword().equals(registerData.getPasswordRepeat())) {
            FacesMessageUtil.addErrorMessage("Passwort und Passwort-Wiederholung müssen übereinstimmen!");
            return null;
        }
        if (this.userService.registerUser(registerData)) {
            this.loginUserManager.setCurrentUser(new UserBean(registerData.getUserName(), registerData.getFirstName(),
                    registerData.getLastName()));
            return "index?faces-redirect=true";
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Die Registrierung ist leider fehlgeschlagen" +
                    "- Der Username existiert bereits!");
            facesContext.addMessage(null, facesMessage);
            return null;
        }

    }
}
