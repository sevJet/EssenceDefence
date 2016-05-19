package io.github.sevjet.essensedefence;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import io.github.sevjet.essensedefence.gui.ITextual;
import io.github.sevjet.essensedefence.field.Inventory;

import java.io.IOException;

public class Gamer implements ITextual {

    protected final Inventory inventory = new Inventory(3, 10);
    protected float gold = 0;

    public Gamer() {
        this(0);
    }

    public Gamer(int gold) {
        this.gold = gold;
    }

    public float getGold() {
        return gold;
    }

    @Deprecated
    public void setGold(float gold) {
        this.gold = gold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean decGold(float gold) {
        if (this.gold - gold < 0)
            return false;
        this.gold -= gold;
        return true;
    }

    public float incGold(float gold) {
        return this.gold += gold;
    }


    @Override
    public String outputValue() {
        return Float.toString(gold);
    }

    @Override
    public boolean isEnded() {
        return (false);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(gold, "gold", 0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        gold = capsule.readFloat("gold", 0f);
    }
}
