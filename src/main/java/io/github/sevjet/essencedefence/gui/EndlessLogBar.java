package io.github.sevjet.essencedefence.gui;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

import io.github.sevjet.essencedefence.game.Player;
import io.github.sevjet.essencedefence.util.Configuration;
import io.github.sevjet.essencedefence.util.Creator;

public class EndlessLogBar extends AbstractControl {

    private Node playerGui = new Node("gui");
    private BitmapText text;

    public EndlessLogBar(Player player) {
        final float dist10m = MovementOnGuiControl.SCALE_FROM_3D_TO_GUI / 10f;

        DataBar bar = new DataBar(5f, 1f / 2f);
        BarControl barC = new BarControl(player, BarMode.EndlessX2);
        bar.addControl(barC);

        text = Creator.text2D("", ColorRGBA.White);
        text.setSize(text.getFont().getCharSet().getRenderedSize() / dist10m);
        text.addControl(new TextControl(player, ""));

        playerGui.attachChild(bar);
        playerGui.attachChild(text);
        playerGui.setLocalScale(dist10m);
        playerGui.setLocalTranslation(
                Configuration.getSettings().getWidth() / 2f,
                Configuration.getSettings().getHeight() - 50f,
                0);
    }

    public Node getGamerGui() {
        return playerGui;
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
