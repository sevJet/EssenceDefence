package io.github.sevjet.essensedefence.entity;

import io.github.sevjet.essensedefence.util.BoxSize;

public class Essence extends Entity3D {

    // FIXME: Not real size, made bigger for been at the center of the tower
    private static final BoxSize SIZE = new BoxSize(2, 2, 1);

    private float damage = 0f;
    private float range = 0f;
    private float speed = 0f;
    private int level = 1;
    private float price = 0f;

    public Essence() {
        super(SIZE);
    }

    public Essence(float damage, float range, float speed, int level, float price) {
        super(SIZE);

        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.level = level;
        this.price = price;
    }

    public float getDamage() {
        return damage;
    }

    public float getRange() {
        return range;
    }

    public float getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }

    public float getPrice() {
        return price;
    }

    public boolean buy() {
        // @TODO Implement buy
        return false;
    }

    public void sell() {
        // @TODO Implement sell
    }

    public boolean upgrade() {
        // @TODO Implement upgrade
        return false;
    }

    public boolean combine() {
        // @TODO Implement combine
        return false;
    }

}
