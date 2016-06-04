package io.github.sevjet.essencedefence;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;

import java.io.IOException;

import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.field.EssenceShop;
import io.github.sevjet.essencedefence.field.Inventory;
import io.github.sevjet.essencedefence.gui.EndlessLogBar;
import io.github.sevjet.essencedefence.gui.ITextual;

public class Gamer implements ITextual {

    protected Inventory inventory = null;
    protected EssenceShop shop = null;
    protected float gold = 0f;
    protected Node gamerGui = null;

    public Gamer() {
        gold = 0f;
    }

    public Gamer(int gold) {
        this.gold = gold;
        inventory = new Inventory(3, 10);
        shop = new EssenceShop(3, 3);
        gamerGui = new Node("gamerGui");

        initShop();
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

        initShop();
    }

    private void initShop() {
        ColorRGBA temp;
        shop.setContent(0, 0, new Essence(5f, 2f, 1f, 1, 10f, ColorRGBA.Red));
        shop.setContent(1, 0, new Essence(3f, 2f, 5f, 1, 10f, ColorRGBA.Yellow));
        shop.setContent(2, 0, new Essence(3f, 4f, 1f, 1, 10f, ColorRGBA.Blue));

        temp = ColorRGBA.Red.clone();
        temp.interpolate(ColorRGBA.Yellow, 0.5f);
        shop.setContent(0, 1, new Essence(5f, 2f, 5f, 1, 20f, temp));

        temp = ColorRGBA.Blue.clone();
        temp.interpolate(ColorRGBA.Red, 0.5f);
        shop.setContent(1, 1, new Essence(5f, 4f, 1f, 1, 20f, temp));

        temp = ColorRGBA.Yellow.clone();
        temp.interpolate(ColorRGBA.Blue, 0.5f);
        shop.setContent(2, 1, new Essence(3f, 4f, 5f, 1, 20f, temp));


        temp = ColorRGBA.Red.clone();
        temp.interpolate(ColorRGBA.Yellow, 0.5f);
        temp.interpolate(ColorRGBA.Blue, 1 / 3f);
        shop.setContent(0, 2, new Essence(5f, 4f, 5f, 1, 30f, temp));

        temp = ColorRGBA.Red.clone();
        temp.interpolate(ColorRGBA.Black, 0.25f);
        shop.setContent(1, 2, new Essence(10f, 2f, 0.5f, 1, 50f, temp));
        temp = ColorRGBA.Yellow.clone();
        temp.interpolate(ColorRGBA.Black, 0.25f);
        shop.setContent(2, 2, new Essence(1f, 2f, 10f, 1, 50f, temp));
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
        capsule.write(inventory, "inventory", null);
        capsule.write(shop, "shop", null);
        capsule.write(gold, "gold", 0f);
        capsule.write(gamerGui, "gamerGui", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        inventory = (Inventory) capsule.readSavable("inventory", new Inventory(3, 10));
        shop = (EssenceShop) capsule.readSavable("shop", new EssenceShop(3, 3));
        gold = capsule.readFloat("gold", 0f);
        gamerGui = (Node) capsule.readSavable("gamerGui", new Node("gamerGui"));
    }
}