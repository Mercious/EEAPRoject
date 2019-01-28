package org.pcConfigurator.entities;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Component")
public class Component extends Article {
    @ManyToOne
    private SlotType requiredSlot;

    protected Component() {}
    public Component(final String articleName) {
        super(articleName);
    }

    public SlotType getRequiredSlot() {
        return requiredSlot;
    }

    public void setRequiredSlot(SlotType requiredSlot) {
        this.requiredSlot = requiredSlot;
    }
}
