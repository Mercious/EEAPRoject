package org.pcConfigurator.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class SlotType {

    @GeneratedValue
    @Id
    private long id;
    // business key
    private String slotName;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlotName() {
        return slotName;
    }

    /**
     * Set's the value of slotName. Will not have any effect if the slotName is already set to a value, since the slot
     * name is the business key of this entity and should not be changed once set.
     *
     * @param slotName The value for the slotName
     */
    public void setSlotName(String slotName) {
        // Pseudo-Immutabilität - wenn slotName einmal gesetzt wurde, dann erlauben wir das Verändern nicht mehr
        if (this.slotName == null)
            this.slotName = slotName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (slotName == null || slotName.isEmpty()) return super.equals(o);
        SlotType slotType = (SlotType) o;
        return Objects.equals(slotName, slotType.slotName);
    }

    @Override
    public int hashCode() {
        if (slotName == null || slotName.isEmpty()) return super.hashCode();
        return Objects.hash(slotName);
    }
}
