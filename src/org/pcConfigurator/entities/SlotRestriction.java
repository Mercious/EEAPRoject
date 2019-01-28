package org.pcConfigurator.entities;

import javax.persistence.*;

@Entity
public class SlotRestriction {
    @Id
    private long id;
    @Enumerated(EnumType.STRING)
    private SlotRestrictionType type;
    @ManyToOne
    private SlotType relatedSlotType;

    private int quantity;

    public SlotRestrictionType getType() {
        return type;
    }

    public void setType(SlotRestrictionType type) {
        this.type = type;
    }

    public SlotType getRelatedSlotType() {
        return relatedSlotType;
    }

    public void setRelatedSlotType(SlotType relatedSlotType) {
        this.relatedSlotType = relatedSlotType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
