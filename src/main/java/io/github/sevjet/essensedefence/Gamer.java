package io.github.sevjet.essensedefence;

import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.control.ITextual;
import io.github.sevjet.essensedefence.control.TextControl;

import static org.lwjgl.opengl.Display.update;

public class Gamer implements ITextual {

    private final AbstractControl control = new TextControl(this, "Gold of gamer:", 1);
    protected float gold = 0;

    public Gamer() {
        this(0);
    }

    public Gamer(int gold) {
        this.gold = gold;

        update();
    }

    public float getGold() {
        return gold;
    }

    @Deprecated
    public void setGold(float gold) {
        this.gold = gold;

        update();
    }

    public boolean decGold(float gold) {
        if (this.gold - gold < 0)
            return false;
        this.gold -= gold;

        update();
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
}
