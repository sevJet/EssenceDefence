package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.building.Building;

import java.io.Serializable;

public class Fortress extends Building implements Serializable {

    protected int health = 100;

    public Fortress() {
        this(100);
    }

    public Fortress(int health) {
        super(3, 3, 4);
        this.setHealth(health);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
