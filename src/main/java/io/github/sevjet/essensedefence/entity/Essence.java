package io.github.sevjet.essensedefence.entity;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;

public class Essence extends Entity3D implements IBuyable {

    // TODO: 19.05.16 Savable
    // FIXME: Not real size, made bigger for been at the center of the tower
    private static final BoxSize SIZE = new BoxSize(1, 1, 1);

    private float damage = 0f;
    private float range = 0f;
    private float speed = 0f;
    private int level = 1;
    private float price = 0f;
    private float offsetX = 0f;
    private float offsetY = 0f;
    private float offsetZ = 0f;

    public Essence() {
        super(SIZE);

        // TODO: 15/05/2016 delete when release
        geometry.getMaterial().setColor("Diffuse", ColorRGBA.randomColor());
    }

    public Essence(float damage, float range, float speed, int level, float price) {
        super(SIZE);

        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.level = level;
        this.price = price;

        // TODO: 15/05/2016 delete when release
        geometry.getMaterial().setColor("Diffuse", ColorRGBA.randomColor());
    }

    public static Essence buy() {
        if (Configuration.getGamer().getGold() >= 10) {
            Configuration.getGamer().decGold(10);
            return new Essence(1, 5, 1, 1, 10);
        }
        return null;
    }

    // TODO: 19.05.16 Check do we need this?
    public static void sell(Tower tower) {
        Configuration.getGamer().incGold(tower.extractCore());
    }

    public float sell() {
        if(geometry.removeFromParent()) {
            Configuration.getGamer().incGold(price);
            return price;
        }
        return 0f;
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

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;

        update();
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;

        update();
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;

        update();
    }

    @Override
    public Vector3f getCenter() {
        return super.getCenter().add(offsetX, offsetY, offsetZ);
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
