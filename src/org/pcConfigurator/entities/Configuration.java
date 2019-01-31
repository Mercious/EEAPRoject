package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/*
    Entity zum Persistieren von Konfigurationen des Kunden
 */
@Entity
public class Configuration {
    @Id
    @GeneratedValue
    private Long id;

    // Ist hier leider ein klassisches "Many-to-Many", das nur über ein Objekt->Instanz Pattern vermeidbar wäre,
    // Welches ich auch nicht wirklich besser fände
    // Die Beziehung bleibt aber Unidirektional, da die Komponenten nicht zu wissen brauchen, in welchen Konfigurationen sie sind
    // (im Normalfall zmnd)
    @ManyToMany
    @JoinTable(name = "configuration_component",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "configuration_id")
    )
    private Set<Article> configuredComponents = Collections.emptySet();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Article> getConfiguredComponents() {
        return configuredComponents;
    }

    public void setConfiguredComponents(Set<Article> configuredComponents) {
        this.configuredComponents = configuredComponents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return super.equals(o);
        Configuration that = (Configuration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id == null) return super.hashCode();
        return Objects.hash(id);
    }
}
