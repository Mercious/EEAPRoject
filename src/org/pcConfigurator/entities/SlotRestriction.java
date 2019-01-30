package org.pcConfigurator.entities;

import javax.persistence.*;

@Entity
public class SlotRestriction {
    @Id
    @GeneratedValue
    private long id;
    @Enumerated(EnumType.STRING)
    private SlotRestrictionType type;
    @ManyToOne
    private SlotType relatedSlotType;

    private int quantity;

    public SlotRestriction() {}
    public SlotRestriction(SlotRestrictionType type, SlotType relatedSlotType, Integer quantity) {
        this.type = type;
        this.relatedSlotType = relatedSlotType;
        this.quantity = quantity;
    }

    public SlotRestriction(SlotType relatedSlotType) {
        this(SlotRestrictionType.REQUIRES, relatedSlotType, 1);
    }

    public SlotRestriction(SlotType relatedSlotType, SlotRestrictionType type) {
        this(type, relatedSlotType, 1);
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
