package io.github.sevjet.essensedefence.gui;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import java.io.IOException;

public class TextControl extends AbstractControl {
    private String textContent;
    private BitmapText text;
    private ITextual entity;

    public TextControl(ITextual object, String textContent) {
        entity = object;
        this.textContent = textContent;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (getSpatial() instanceof BitmapText) {
            text = (BitmapText) getSpatial();
        } else {
            throw new ClassCastException(getSpatial().getClass().toString() +
                    " must be BitmapText");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        text.setText(textContent + " " + entity.outputValue());
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
