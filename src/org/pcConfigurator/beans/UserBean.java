package org.pcConfigurator.beans;

import java.io.Serializable;

/*
    Reduzierte UserDaten, die zum eindeutigen Idenzifizieren eines Users (businesskey userName) reichen und auch dem Frontend genügen
    (Anzeige Vorname, Nachname)
 */
public class UserBean implements Serializable {
    private String firstName;
    private String lastName;
    private String userName;

    public UserBean() {
    }

    public UserBean(final String userName, final String firstName, final String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
