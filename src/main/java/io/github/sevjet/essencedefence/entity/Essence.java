package io.github.sevjet.essencedefence.entity;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import java.io.IOException;

import io.github.sevjet.essencedefence.util.BoxSize;
import io.github.sevjet.essencedefence.util.Configuration;

public class Essence extends Entity3D implements IBuyable {

    private static final BoxSize SIZE = new BoxSize(1, 1, 1);

    private static final float damageK = 1.25f;
    private static final float rangeK = 1.03f;
    private static final float speedK = 1.1f;
    private static final float priceK = 2.0f;

    private float offsetX = 0f;
    private float offsetY = 0f;
    private float offsetZ = 0f;

    private float damage = 0f;
    private float range = 0f;
    private float speed = 0f;
    private int level = 1;
    private float price = 0f;

    private ColorRGBA color;

    public Essence() {
        super(SIZE);

        color = ColorRGBA.randomColor();
        geometry.getMaterial().setColor("Diffuse", color);
    }

    public Essence(float damage, float range, float speed, int level, float price) {
        this(damage, range, speed, level, price, ColorRGBA.randomColor());
    }

    public Essence(float damage, float range, float speed, int level, float price, ColorRGBA color) {
        super(SIZE);

        this.damage = trim(damage);
        this.range = trim(range);
        this.speed = trim(speed);
        this.level = level;
        this.price = trim(price);

        this.color = color;

        geometry.getMaterial().setColor("Diffuse", color);
    }

    public Essence(Essence essence) {
        this(essence.damage,
                essence.range,
                essence.speed,
                essence.level,
                essence.price,
                essence.color.clone());
    }

    public Essence buySame() {
        if (Configuration.getGamer().decGold(price)) {
            return new Essence(this);
        }
        return null;
    }

    public static Essence buy() {
        Essence essence = getNew();
        if (Configuration.getGamer().decGold(essence.getPrice())) {
            return essence;
        }
        return null;
    }

    public static Essence getNew() {
        return new Essence(2f, 3f, 1f, 1, 10f);
    }

    public float sell() {
        Configuration.getGamer().incGold(price);
        geometry.removeFromParent();

        return price;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
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
        if (Configuration.getGamer().decGold(price)) {
            level++;
            damage = trim(damage * damageK);
            range = trim(range * rangeK);
            speed = trim(speed * speedK);
            price = trim(price * priceK);

            System.out.println("UPGRADE");
            return true;
        }
        return false;
    }

    public boolean combine(Essence essence) {
        float price = trim((this.price + essence.price) * 1.25f);

        if (Configuration.getGamer().decGold(price)) {
//            int level = this.level + essence.level;
//            float rightK = this.level * 1.0f / level;
//            float leftK = essence.level * 1.0f / level;
            this.damage += essence.damage;
            this.range = trim((this.range + essence.range) / 1.75f);
            this.speed += essence.speed;
            this.level += essence.level;
            this.price = trim(price * priceK);

            color.interpolate(essence.getColor(), 0.5f);
            geometry.getMaterial().setColor("Diffuse", color);

            return true;
        }
        return false;
    }

    private float trim(final float num) {
        return FastMath.floor(num * 100f) / 100f;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(offsetX, "offsetX", 0f);
        capsule.write(offsetY, "offsetY", 0f);
        capsule.write(offsetZ, "offsetZ", 0f);

        capsule.write(damage, "damage", 0f);
        capsule.write(range, "range", 0f);
        capsule.write(speed, "speed", 0f);
        capsule.write(level, "level", 1);
        capsule.write(price, "price", 0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        offsetX = capsule.readFloat("offsetX", 0f);
        offsetY = capsule.readFloat("offsetY", 0f);
        offsetZ = capsule.readFloat("offsetZ", 0f);

        damage = capsule.readFloat("damage", 0f);
        range = capsule.readFloat("range", 0f);
        speed = capsule.readFloat("speed", 0f);
        level = capsule.readInt("level", 1);
        price = capsule.readFloat("price", 0f);
    }

    public Essence clone() {
        Essence essence = new Essence(damage, range, speed, level, price);
        essence.setGeometry(geometry.clone());
        return essence;
    }

    public String getInfo() {
        return "Level: " + getLevel() + '\n' +
                "Damage: " + getDamage() + '\n' +
                "Range: " + getRange() + '\n' +
                "Speed: " + getSpeed() + '\n' +
                "Price (buy): " + getPrice();
    }
}
