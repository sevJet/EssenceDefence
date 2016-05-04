package io.github.sevjet.essensedefence;

public class Fortress extends Building {

    protected int health = 100;

    public Fortress() {
        this(100);
    }

    public Fortress(int health) {
        this.setHealth(health);
        setWidth(3);
        setHeight(3);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
