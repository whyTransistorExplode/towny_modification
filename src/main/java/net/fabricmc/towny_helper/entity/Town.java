package net.fabricmc.towny_helper.entity;

public class Town {
    private String name;
    private String icon;
    private int x;
    private int y;
    private int z;

    public Town(String name, int x, int y, int z,String icon) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
