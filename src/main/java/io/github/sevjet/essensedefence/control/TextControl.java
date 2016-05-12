package io.github.sevjet.essensedefence.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

import java.io.IOException;

public class TextControl extends AbstractControl {

    private String textContent;
    private BitmapText text;
    private ITextual entity;

    private TextControl(ITextual object) {
        entity = object;
        Configuration.getGuiNode().addControl(this);
    }

    public TextControl(ITextual object, String textContent, int rowNum) {
        this(object);
        this.textContent = textContent;
        text = Creator.text(textContent, rowNum);
    }


    @Override
    protected void controlUpdate(float tpf) {
        if (entity.isEnded())
            halt();
        text.setText(textContent + " " + entity.outputValue());
    }

    private void halt() {
        Configuration.getGuiNode().detachChild(text);
        Configuration.getGuiNode().removeControl(this);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(textContent, "textContent", null);
        capsule.write(text, "text", null);
        capsule.write(entity, "entity", entity);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        textContent = capsule.readString("textContent", null);
        text = (BitmapText) capsule.readSavable("text", null);
        entity = (ITextual) capsule.readSavable("entity", null);
    }

}
