package io.github.sevjet.essensedefence.entity;

import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;

public class Essence extends Entity3D implements IBuyable {

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

    public static Essence buy() {
        if (Configuration.getGamer().getGold() >= 10) {
            Configuration.getGamer().decGold(10);
            return new Essence(1, 5, 1, 1, 0);
        }
        return null;
    }

    public static void sell(Tower tower) {
        tower.extractionCore();
        Configuration.getGamer().incGold(10);
    }

    public float sell() {
        if (geometry.getParent() != null) {
            geometry.getParent().detachChild(geometry);
        }
        Configuration.getGamer().incGold(price);
        return price;
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

    @Override
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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
