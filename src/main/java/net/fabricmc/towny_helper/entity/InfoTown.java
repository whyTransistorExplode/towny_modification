package net.fabricmc.towny_helper.entity;

public class InfoTown {
    private String name;
    private int kbSize;

    public InfoTown(String name, int kbSize) {
        this.name = name;
        this.kbSize = kbSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKbSize() {
        return kbSize;
    }

    public void setKbSize(int kbSize) {
        this.kbSize = kbSize;
    }
}
