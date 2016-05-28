package io.github.sevjet.essencedefence.gui;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.util.Creator;
import io.github.sevjet.essencedefence.util.Getter;

public class GuiControl extends AbstractControl {
    private Entity entity;
    private ITextual object;
    private String textName;

    private Node controlNode = new Node("gui");
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
        bar.addControl(new BarControl(object));
        controlNode.attachChild(bar);

        text = Creator.text2D(textName, ColorRGBA.White);
        text.setSize(0.3f);
        text.setLocalTranslation(-0.3f, 0.4f, 0);
        text.addControl(new TextControl((ITextual) entity, textName));
        controlNode.attachChild(text);

        controlNode.addControl(new MovementOnGuiControl(entity));
    }

    public Node getControlNode() {
        return controlNode;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (object.isEnded()) {
            controlNode.removeFromParent();
            getSpatial().removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
