package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SlotRestriction {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private SlotRestrictionType type;
    @ManyToOne
    private SlotType relatedSlotType;

    private int quantity;

    public SlotRestriction() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return super.equals(o);
        SlotRestriction that = (SlotRestriction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id == null) return super.hashCode();
        return Objects.hash(id);
    }
}
