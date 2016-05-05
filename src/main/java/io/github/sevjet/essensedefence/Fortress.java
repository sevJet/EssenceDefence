package io.github.sevjet.essensedefence;

import java.io.Serializable;

public class Fortress extends Building implements Serializable {

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
