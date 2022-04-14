package net.fabricmc.towny_helper.entity;

public class Player {
    private int x;
    private int y;
    private int z;
    private String name;
    private int health;
    private String world;

    public Player() {
    }

    public Player(int x, int y, String name, String world) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.world = world;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return world;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
