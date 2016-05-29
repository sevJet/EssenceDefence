package io.github.sevjet.essencedefence.entity.monster;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;

import java.io.IOException;

import io.github.sevjet.essencedefence.control.BasicControl;
import io.github.sevjet.essencedefence.control.MonsterControl;
import io.github.sevjet.essencedefence.entity.Entity3D;
import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.gui.ITextual;
import io.github.sevjet.essencedefence.util.BoxSize;
import io.github.sevjet.essencedefence.util.Configuration;

public class Monster extends Entity3D implements ITextual {

    private static final BoxSize SIZE = new BoxSize(1, 1, 1);

    private BasicControl control = new MonsterControl();
    private float health = 0f;
    private float speed = 0f;
    private float damage = 0f;
    private float exp = 1f;
    private float money = 10f;

    public Monster() {
        super(SIZE);
        geometry.addControl(control);
    }

    public Monster(float health, float speed, float damage) {
        super(SIZE);

        this.health = health;
        this.speed = speed;
        this.damage = damage;
        geometry.addControl(control);
    }

    public Monster(int x, int y, float health, float speed, float damage) {
        super(x, y, SIZE);

        this.health = health;
        this.speed = speed;
        this.damage = damage;
        geometry.addControl(control);
    }

    public static Monster getDefaultMonster() {
        return new Monster(10f, 1f, 10f);
    }

    public float getHealth() {
        return health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public MapField getField() {
        Field field = super.getField();
        if (field instanceof MapField) {
            return (MapField) field;
        }
        return null;
    }

    public double hit(float damage) {
        this.health = FastMath.floor((this.health - damage) * 100f) / 100f;

        if (this.health <= 0f) {
            giveReward();
            die();
        }
        return this.health;
    }

    private void giveReward() {
        Configuration.getGamer().incGold(money);
    }

    public void upgrade(int level) {
        float k = 1f + (level - 1) / 10f;
        health *= k;
        money *= k;
    }

    // TODO: 12/05/2016 change to protected
    @Deprecated
    public void die() {
        Field field = getField();
        if (field != null) {
            field.removeObject(this);
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(control, "control", null);
        capsule.write(health, "health", 0f);
        capsule.write(speed, "speed", 0f);
        capsule.write(damage, "damage", 0f);
        capsule.write(exp, "exp", 1f);
        capsule.write(money, "money", 10f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        control = (BasicControl) capsule.readSavable("control", null);
        health = capsule.readFloat("health", 0f);
        speed = capsule.readFloat("speed", 0f);
        damage = capsule.readFloat("damage", 0f);
        exp = capsule.readFloat("exp", 1f);
        money = capsule.readFloat("money", 10f);
    }

    @Override
    public String outputValue() {
        return Float.toString(health);
    }

    @Override
    public boolean isEnded() {
        return health <= 0 || geometry == null || geometry.getParent() == null || getField() == null;
    }
}

