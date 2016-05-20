package io.github.sevjet.essensedefence;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.field.Inventory;
import io.github.sevjet.essensedefence.gui.*;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

import java.io.IOException;

import static io.github.sevjet.essensedefence.gui.MovementOnGuiControl.SCALE_FROM_3D_TO_GUI;

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

    public void setGui() {
        Configuration.getGuiNode().addControl(new EndlessLogBar(this));

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

// TODO: 20/05/2016 move from this class & change
class EndlessLogBar extends AbstractControl {
    private Node gamerGui = new Node();
    private BitmapText text;
    private DataBar bar;
    private BarControl barC;
    private Gamer gamer;

    public EndlessLogBar(Gamer gamer) {
        this.gamer = gamer;
        float dist10m = SCALE_FROM_3D_TO_GUI / 10f;

        bar = new DataBar(5f, 1f / 2f);
        barC = new BarControl(gamer, BarMode.EndlessX2);
        bar.addControl(barC);

        text = Creator.text2D("", ColorRGBA.White);
        text.setSize(text.getFont().getCharSet().getRenderedSize() / dist10m);
        text.addControl(new TextControl(gamer, ""));

        gamerGui.attachChild(bar);
        gamerGui.attachChild(text);
        gamerGui.setLocalScale(dist10m);
        gamerGui.setLocalTranslation(
                Configuration.getSettings().getWidth() / 2f,
                Configuration.getSettings().getHeight() - 50f,
                0);

        Configuration.getGuiNode().attachChild(gamerGui);
    }

    @Override
    protected void controlUpdate(float tpf) {
        text.setLocalTranslation(
                -text.getLineWidth() / 2f,
                text.getLineHeight() / 2f,
                0);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}