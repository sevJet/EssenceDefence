package io.github.sevjet.essensedefence.gui;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.Getter;

public class GuiControl extends AbstractControl {
    private Entity entity;
    private ITextual object;
    private String textName;

    private Node controlNode = new Node();
    private DataBar bar;

    public GuiControl(Entity entity, String name, float width, float height) {
        BitmapText text;

        if (entity instanceof ITextual) {
            this.object = (ITextual) entity;
        } else {
            throw new ClassCastException(Getter.getEntity(getSpatial()).getClass().toString() +
                    " must implement ITextual interface!!!");
        }
        this.entity = entity;
        this.textName = name;

        bar = new DataBar(width, height);
        bar.addControl(new BarControl(entity));
        controlNode.attachChild(bar);

        text = Creator.text2D(textName, ColorRGBA.White);
        text.setSize(0.3f);
        text.setLocalTranslation(-0.3f, 0.4f, 0);
        text.addControl(new TextControl((ITextual) entity, textName));
        controlNode.attachChild(text);

        Configuration.getGuiNode().attachChild(controlNode);
        controlNode.scale(2 * text.getFont().getCharSet().getRenderedSize());

        controlNode.addControl(new MovementOnGuiControl(entity));
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (object.isEnded()) {
            controlNode.removeFromParent();
            getSpatial().removeControl(this);
            return;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
