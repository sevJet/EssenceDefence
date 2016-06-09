package io.github.sevjet.essencedefence.game;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.scene.Node;

import java.io.IOException;

import io.github.sevjet.essencedefence.field.Inventory;
import io.github.sevjet.essencedefence.gui.EndlessLogBar;
import io.github.sevjet.essencedefence.gui.ITextual;

public class Player implements ITextual, Savable {

    protected Node gui = null;
    private float gold;
    private Inventory inventory;

    public Player() {
        gold = 0f;
        inventory = new Inventory(3, 10);

        initGui();
    }

    public Player(final float gold) {
        if (gold < 0f) {
            throw new IllegalArgumentException("Gold initial value can not be less then 0");
        }

        this.gold = gold;
        inventory = new Inventory(3, 10);

        initGui();
    }

    public void initGui() {
        gui = new Node("playerGui");

        final EndlessLogBar bar = new EndlessLogBar(this);
        gui.addControl(bar);
        gui.attachChild(bar.getGamerGui());
    }

    public float getGold() {
        return gold;
    }

    public Inventory inventory() {
        return inventory;
    }

    public boolean take(float amount) {
        if (amount <= 0f) {
            throw new IllegalArgumentException("Amount can not be less then or equal to 0");
        }
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public float give(float amount) {
        if (amount <= 0f) {
            throw new IllegalArgumentException("Amount can not be less then or equal to 0");
        }
        gold += amount;
        return gold;
    }


    @Override
    public String outputValue() {
        return Float.toString(gold);
    }

    @Override
    public boolean isEnded() {
        return false;
    }

    public Node getGui() {
        return gui;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);

        capsule.write(inventory, "inventory", null);
        capsule.write(gold, "gold", 0f);
        capsule.write(gui, "gui", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);

        inventory = (Inventory) capsule.readSavable("inventory", new Inventory(3, 10));
        gold = capsule.readFloat("gold", 0f);
        Node tmp = (Node) capsule.readSavable("gui", null);
        if (tmp == null) {
            initGui();
        } else {
            gui = tmp;
        }
    }

}
