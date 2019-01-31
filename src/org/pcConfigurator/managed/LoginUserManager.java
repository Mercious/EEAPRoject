package org.pcConfigurator.managed;

import org.pcConfigurator.annotations.CurrentUser;
import org.pcConfigurator.beans.UserBean;
import org.pcConfigurator.entities.User;
import org.pcConfigurator.services.UserService;
import org.pcConfigurator.util.FacesMessageUtil;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@SessionScoped
@Named
public class LoginUserManager implements Serializable {
    @NotNull
    @Size(min = 5, max = 100)
    private String userName;
    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    private UserBean currentUser;

    @Inject
    private UserService userService;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        User loggedUser = this.userService.loginUser(userName, password);
        if (loggedUser != null ) {
            this.setCurrentUser(new UserBean(loggedUser.getUserName(), loggedUser.getFirstName(), loggedUser.getLastName()));
            return "index.xhtml?face-redirect=true";
        }

        FacesMessageUtil.addErrorMessage("Fehler beim Login, Login-Daten sind ungültig. Bitte versuchen Sie es noch einmal!");
        return null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void setCurrentUser(final UserBean currentUser) {
        this.currentUser = currentUser;
    }

    @Produces
    @CurrentUser
    public UserBean getCurrentUser() {
        return currentUser;
    }
}
