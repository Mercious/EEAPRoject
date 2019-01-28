package org.pcConfigurator.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Objects;
import java.util.Set;

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
     *  Set's the value of slotName. Will not have any effect if the slotName is already set to a value, since the slot
     *  name is the business key of this entity and should not be changed once set.
     * @param slotName The value for the slotName
     */
    public void setSlotName(String slotName) {
        // Pseudo-Immutabilität - wenn slotName einmal gesetzt wurde, dann erlauben wir das Verändern nicht mehr
        if(this.slotName == null)
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
        SlotType slotType = (SlotType) o;
        return Objects.equals(slotName, slotType.slotName);
    }

    // Hash code contract über Stabilität kann in JPA nicht gehalten werde, da durch den erzwungen public Konstruktor
    // die Immutabilität der business-keys nicht gewährleistet werden kann.
    // => Entwickler wissen das idR, sind sich also bewusst, dass sie Entity-Typen nicht in hash-Collections
    // legen, bevor sie den business key gesetzt haben
    @Override
    public int hashCode() {
        return Objects.hash(slotName);
    }
}
