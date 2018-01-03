package io.github.sevjet.essensedefence;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.field.EssenceShop;
import io.github.sevjet.essensedefence.field.Inventory;
import io.github.sevjet.essensedefence.gui.*;

import java.io.IOException;

public class Gamer implements ITextual {

    protected Inventory inventory = new Inventory(3, 10);
    protected EssenceShop shop = new EssenceShop(3, 3);
    protected float gold = 0;
    protected Node gamerGui = new Node("gamerGui");

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


    public void resetInventory() {
        this.inventory = new Inventory(3, 10);
    }

    public EssenceShop getShop() {
        return shop;
    }

    public void resetShop() {
        this.shop = new EssenceShop(3, 3);

        shop.setContent(0, 0, new Essence(5f, 2f, 1f, 1, 10f, ColorRGBA.Red));
        shop.setContent(1, 0, new Essence(3f, 2f, 5f, 1, 10f, ColorRGBA.Yellow));
        shop.setContent(2, 0, new Essence(3f, 4f, 1f, 1, 10f, ColorRGBA.Blue));

        shop.setContent(0, 1, new Essence(5f, 2f, 5f, 1, 20f, ColorRGBA.Red.add(ColorRGBA.Yellow)));
        shop.setContent(1, 1, new Essence(5f, 4f, 1f, 1, 20f, ColorRGBA.Blue.add(ColorRGBA.Red)));
        shop.setContent(2, 1, new Essence(3f, 4f, 5f, 1, 20f, ColorRGBA.Yellow.add(ColorRGBA.Blue)));

        shop.setContent(0, 2, new Essence(5f, 4f, 5f, 1, 30f, ColorRGBA.Red.add(ColorRGBA.Yellow).add(ColorRGBA.Blue)));
        shop.setContent(1, 2, new Essence(10f, 2f, 0.5f, 1, 50f, ColorRGBA.Red.add(new ColorRGBA(-0.25f, -0.25f, -0.25f, -0.1f))));
        shop.setContent(2, 2, new Essence(1f, 2f, 10f, 1, 50f, ColorRGBA.Yellow.add(new ColorRGBA(-0.25f, -0.25f, -0.25f, -0.1f))));

//        for (int i = 0; i < shop.getRows(); i++) {
//            for (int j = 0; j < shop.getCols(); j++) {
//                float damage, range, speed, price;
//                damage = 0.1f + (float) Math.random() * 5;
//                range = 2f + (float) Math.random() * 3;
//                speed = 0.5f + (float) Math.random() * 5;
//                price = damage + range + speed;
//                Essence essence = new Essence(damage, range, speed, 1, price);
//                shop.setContent(i, j, essence);
//            }
//        }
    }

    public boolean decGold(float gold) {
        if (this.gold - gold < 0)
            return false;
        this.gold = FastMath.floor((this.gold - gold) * 100f) / 100f;
        return true;
    }


    public float incGold(float gold) {
        return this.gold = FastMath.floor((this.gold + gold) * 100f) / 100f;
    }

    @Override
    public String outputValue() {
        return Float.toString(gold);
    }

    @Override
    public boolean isEnded() {
        return (false);
    }

    public void setGui() {
        gamerGui = new Node("gamerGui");
        EndlessLogBar bar = new EndlessLogBar(this);
        gamerGui.addControl(bar);
        gamerGui.attachChild(bar.getGamerGui());
    }


    public Node getGui() {
        return gamerGui;
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
