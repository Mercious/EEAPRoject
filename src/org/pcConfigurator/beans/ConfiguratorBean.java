package org.pcConfigurator.beans;

import org.pcConfigurator.entities.Component;
import org.pcConfigurator.entities.Motherboard;

import java.util.List;

/**
 * Simplerer Repräsentation einer Konfiguration für das Frontend -> Slots sind hardcoded, damit sie einfach ansprechbar sind
 *
 */
public class ConfiguratorBean {
    private Motherboard motherboard;
    private Component CPU;
    private Component GPU;
    private Component RAM;
    private Component PSU;
    private List<Component> accessories;

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public Component getCPU() {
        return CPU;
    }

    public void setCPU(Component CPU) {
        this.CPU = CPU;
    }

    public Component getGPU() {
        return GPU;
    }

    public void setGPU(Component GPU) {
        this.GPU = GPU;
    }

    public Component getRAM() {
        return RAM;
    }

    public void setRAM(Component RAM) {
        this.RAM = RAM;
    }

    public Component getPSU() {
        return PSU;
    }

    public void setPSU(Component PSU) {
        this.PSU = PSU;
    }

    public List<Component> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Component> accessories) {
        this.accessories = accessories;
    }
}
