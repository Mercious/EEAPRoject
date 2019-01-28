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
    @ManyToMany
    @JoinTable(name = "configuration_component",
            joinColumns = @JoinColumn(name = "component_id"),
            inverseJoinColumns = @JoinColumn(name = "configuration_id")
    )
    private Set<Component> configuredComponents = Collections.emptySet();
    @ManyToOne
    private Motherboard motherboard;

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

    public Set<Component> getConfiguredComponents() {
        return configuredComponents;
    }

    public void setConfiguredComponents(Set<Component> configuredComponents) {
        this.configuredComponents = configuredComponents;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }
}
