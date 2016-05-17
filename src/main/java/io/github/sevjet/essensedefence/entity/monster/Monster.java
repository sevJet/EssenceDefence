package io.github.sevjet.essensedefence.entity.monster;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import io.github.sevjet.essensedefence.control.BasicControl;
import io.github.sevjet.essensedefence.control.MonsterControl;
import io.github.sevjet.essensedefence.entity.Entity3D;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.MapField;
import io.github.sevjet.essensedefence.gui.ITextual;
import io.github.sevjet.essensedefence.gui.Text3dControl;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;

import java.io.IOException;

public class Monster extends Entity3D implements ITextual {

    private static final BoxSize SIZE = new BoxSize(1, 1, 1);
    // FIXME: 16/05/2016
    boolean f = false;
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
        this.health -= damage;

        if (this.health <= 0f) {
            giveReward();
            die();
        }
        return this.health;
    }

    private void giveReward() {
        Configuration.getGamer().incGold(money);
    }

    // TODO: 12/05/2016 change to protected
    @Deprecated
    public void die() {
        Field field = getField();
        if (field != null) {
            field.removeObject(this);
        }
        System.out.println("Monster.die()");
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
        return health <= 0 || geometry == null || geometry.getParent() == null;
    }

    @Override
    protected boolean update() {
        // TODO: 16/05/2016 create method spawn
        if (super.update() && !f && geometry != null && geometry.getParent() != null) {
            new Text3dControl(this, "XP:");
            f = true;
        }
        return true;
    }
}

