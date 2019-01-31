package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

/*
    Entity zum Persistieren von Konfigurationen des Kunden
 */
@Entity
public class Configuration {
    @Id
    @GeneratedValue
    private Double id;
    // Wer hat diese Konfiguration erstellt, also wem gehört sie?
    @ManyToOne
    private User creator;

    // Ist hier leider ein klassisches "Many-to-Many", das nur über ein Objekt->Instanz Pattern vermeidbar wäre,
    // Welches ich auch nicht wirklich besser fände
    @ManyToMany
    @JoinTable(name = "configuration_component",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "configuration_id")
    )
    private Set<Article> configuredComponents = Collections.emptySet();

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


    public Set<Article> getConfiguredComponents() {
        return configuredComponents;
    }

    public void setConfiguredComponents(Set<Article> configuredComponents) {
        this.configuredComponents = configuredComponents;
    }
}
