package org.pcConfigurator.beans;


import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.SlotType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Repräsentiert die aktuelle Konfiguration eines Nutzers - also nicht persistiert. Wir verwenden hier eine etwas andere
 * Darstellung dieser Konfiguration, die es uns vor allem erlaubt die Kompatbilität der Teile zueinander über mehrere
 * Konfigurationsschritte hinweg zu beurteilen.
 * Ultimativ zählt dafür nur das Verhältnis von requiredSlots : providedSlots, welches stimmen muss. Dies wird durch die
 * ensptrechenden Kompatibilitäts-Strategien, siehe org.pcConfigurator.strategies.CompatibilityStrategy, beurteilt.
 */
public class ConfigurationBean {

    private HashMap<Article, Integer> configuredComponents = new HashMap<>();
    private HashMap<SlotType, Integer> totalRequiredSots = new HashMap<>();
    private HashMap<SlotType, Integer> totalProvidedSlots = new HashMap<>();

    public HashMap<Article, Integer> getConfiguredComponents() {
        return configuredComponents;
    }

    public void setConfiguredComponents(final HashMap<Article, Integer> configuredComponents) {
        this.configuredComponents = configuredComponents;
    }

    public void addComponent(final Article component, final Integer amount) {
        this.configuredComponents.merge(component, amount, (a, b) -> a + b);
    }

    public void removeComponent(final Article component, final Integer amount) {
        if (this.configuredComponents.get(component) == null)
            return;
        if (this.configuredComponents.get(component) <= amount)
            this.configuredComponents.remove(component);
        else
            this.configuredComponents.merge(component, amount, (a,b) -> a - b);
    }

    public HashMap<SlotType, Integer> getTotalProvidedSlots() {
        return totalProvidedSlots;
    }

    public void setTotalProvidedSlots(HashMap<SlotType, Integer> totalProvidedSlots) {
        this.totalProvidedSlots = totalProvidedSlots;
    }

    public void addProvidedSlots(HashMap<SlotType, Integer> providedSlots) {
        for (SlotType slotType : providedSlots.keySet()) {
            this.totalProvidedSlots.merge(slotType, providedSlots.get(slotType), (a,b) -> a + b);
        }
    }

    public void removeProvidedSlot(final SlotType slot, final Integer amount) {
        if (this.totalProvidedSlots.get(slot) == null)
            return;
        if (this.totalProvidedSlots.get(slot) <= amount)
            this.totalProvidedSlots.remove(slot);
        else
            this.totalProvidedSlots.merge(slot, amount, (a,b) -> a - b);
    }

    public void removeProvidedSlots(final HashMap<SlotType, Integer> slots) {
        for (SlotType slotType : slots.keySet()) {
            removeProvidedSlot(slotType, slots.get(slotType));
        }
    }

    public HashMap<SlotType, Integer> getTotalRequiredSots() {
        return totalRequiredSots;
    }

    public void addRequiredSlots(HashMap<SlotType, Integer> requiredSlots) {
        for (SlotType slotType : requiredSlots.keySet()) {
            this.totalRequiredSots.merge(slotType, requiredSlots.get(slotType), (a,b) -> a + b);
        }
    }

    public void removeRequiredSlot(final SlotType requiredSlot, final Integer amount) {
        if (this.totalRequiredSots.get(requiredSlot) == null)
            return;
        if (this.totalRequiredSots.get(requiredSlot) <= amount)
            this.totalRequiredSots.remove(requiredSlot);
        else
            this.totalRequiredSots.merge(requiredSlot, amount, (a,b) -> a - b);
    }

    public void removeRequiredSlots(final HashMap<SlotType, Integer> requiredSlots) {
        for (SlotType slotType : requiredSlots.keySet()) {
            removeRequiredSlot(slotType, requiredSlots.get(slotType));
        }
    }

    public void setTotalRequiredSots(final HashMap<SlotType, Integer> requiredSots) {
        this.totalRequiredSots = requiredSots;
    }
}
