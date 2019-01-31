package org.pcConfigurator.entities;


import javax.persistence.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String userName;
    private String firstName;
    private String lastName;
    private String eMail;

    private String streetName;
    private String streetNumber;
    private String postalCode;
    private String city;
    private String country;
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Configuration> savedConfigurations = Collections.emptySet();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Configuration> getSavedConfigurations() {
        return savedConfigurations;
    }

    public void setSavedConfigurations(final Set<Configuration> savedConfigurations) {
        this.savedConfigurations = savedConfigurations;
    }

    public void addConfiguration(final Configuration configuration) {
        this.savedConfigurations.add(configuration);
    }

    public void removeConfiguration(final Configuration configuration) {
        this.savedConfigurations.remove(configuration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (userName == null || userName.isEmpty()) return super.equals(o);
        User user = (User) o;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        if (userName == null || userName.isEmpty()) return super.hashCode();
        return Objects.hash(userName);
    }
}
