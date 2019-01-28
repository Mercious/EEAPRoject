package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/*
    Entity zum Persistieren von Konfigurationen des Kunden
 */
@Entity
public class Configuration {
    @Id
    private long id;
    // Wer hat diese Konfiguration erstellt, also wem gehört sie?
    @ManyToOne
    private User creator;

    // Ist hier leider ein klassisches "Many-to-Many", das nur über ein Objekt->Instanz Pattern vermeidbar wäre,
    // Welches ich auch nicht wirklich besser fände
    @ManyToMany
    @JoinTable(name = "configuration_component",
            joinColumns = @JoinColumn(name = "component_id"),
            inverseJoinColumns = @JoinColumn(name = "configuration_id")
    )
    private Set<Article> configuredComponents = Collections.emptySet();

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
