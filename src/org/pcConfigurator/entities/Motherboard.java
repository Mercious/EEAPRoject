package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
@DiscriminatorValue("Motherboard")
public class Motherboard extends Article {
    // for the sake of simplicity we will ignore the amount of each slot that the motherboard supports. Counting them
    // would produce a lot of boilerplate, hardcoded slot-specific logic that I would like to avoid as it adds no value
    // in terms of "lessons learned"
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "motherboard_slottype",
            joinColumns = @JoinColumn(name = "motherboard_id"),
            inverseJoinColumns = @JoinColumn(name = "slottype_id")
    )
    private Set<SlotType> providedSlots = Collections.emptySet();

    public Motherboard(final String articleName) {
        super(articleName);
        super.setType(ComponentType.MB);
    }

    protected Motherboard() {
       this("");
    }

    @Override
    public void setType(ComponentType type) {
        if (ComponentType.MB.equals(type))
            super.setType(type);
        else
            throw new IllegalArgumentException("Motherboard can only be of type ComponentType.MB (already set at construction time)!");
    }

    public Set<SlotType> getProvidedSlots() {
        return Collections.unmodifiableSet(providedSlots);
    }

    public void setProvidedSlots(Set<SlotType> providedSlots) {
        this.providedSlots = providedSlots;
    }
}
