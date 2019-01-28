package org.pcConfigurator.managed;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ViewScoped
@Named
public class RegisterData implements Serializable {
    @NotNull
    @Size (min = 2, max = 100)
    private String firstName;
    @NotNull
    @Size(min = 2, max = 100)
    private String lastName;
    @NotNull
    @Size(min = 5, max = 20)
    private String userName;
    @NotNull
    @Size(max = 100)
    // E-Mail Validation RegEx sind extrem lange und entsprechend ineffizient, daher hier ausgelassen.
    // Daher kann meinetwegen der User jeden Unsinn als E-Mail eingeben -> in einer realen Anwendung könnte er den Account
    // erst nutzen, nachdem er eine sinnvolle E-Mail eingegeben hat, die Validierung würde über das reale Versenden einer
    // E-Mail an diese Adresse erfolgen
    private String email;
    @NotNull
    @Size(max = 100)
    private String street;
    @NotNull
    @Size(max = 100)
    private String city;
    @NotNull
    @Pattern(regexp = "[0-9]+[a-zA-Z]?")
    @Size(max = 10)
    private String streetNumber;
    @NotNull
    @Size(min = 5, max = 100)
    private String password;
    @NotNull
    @Size(min = 5, max = 100)
    private String passwordRepeat;
    @NotNull
    @Pattern(regexp = "(?!01000|99999)(0[1-9]\\d{3}|[1-9]\\d{4})")
    private String zipcode;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
