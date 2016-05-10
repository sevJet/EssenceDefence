package io.github.sevjet.essensedefence.control;

import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

public class TextControl extends AbstractControl {

    private String textContent;
    private BitmapText text;
    private Object entity;

    public TextControl(Object object) {
        entity = object;
        Configuration.getGuiNode().addControl(this);
    }

    public TextControl(Object object, String textContent, int rowNum) {
        this(object);
        this.textContent = textContent;
        text = Creator.text(textContent, rowNum);
    }


    @Override
    protected void controlUpdate(float tpf) {
        if (entity instanceof ITextual) {
            ITextual obj = ((ITextual) entity);
            text.setText(textContent + " " + obj.outputValue());
            if (obj.isEnded())
                halt();
            return;
        } else throw new ClassCastException(entity.getClass() + " must implement ITextual interface");
    }

    private void halt() {
        Configuration.getGuiNode().detachChild(text);
        Configuration.getGuiNode().removeControl(this);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
