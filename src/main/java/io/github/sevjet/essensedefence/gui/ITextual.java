package io.github.sevjet.essensedefence.gui;


import com.jme3.export.*;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

import java.io.IOException;

public interface ITextual extends Savable {
    default void newText(String textName, int rowNum) {
        class TextControl extends AbstractControl {

            private String textContent;
            private String outputValue;
            private BitmapText text;
            private ITextual entity;

            private TextControl(ITextual object) {
                entity = object;
                Configuration.getGuiNode().addControl(this);
            }

            public TextControl(ITextual object, String textContent, int rowNum) {
                this(object);
                this.textContent = textContent;
                this.outputValue = "";
                text = Creator.text(textContent, rowNum);
            }


            // TODO: 19.05.16 hot point, remake
            @Override
            protected void controlUpdate(float tpf) {
                if (entity.isEnded())
                    halt();
                String tmp = entity.outputValue();
                if(outputValue.equals(tmp)) {
                    outputValue = tmp;
                    text.setText(textContent + " " + tmp);
                }
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
                capsule.write(outputValue, "outputValue", "");
                capsule.write(text, "text", null);
                capsule.write(entity, "entity", entity);
            }

            @Override
            public void read(JmeImporter im) throws IOException {
                super.read(im);

                InputCapsule capsule = im.getCapsule(this);
                textContent = capsule.readString("textContent", null);
                outputValue = capsule.readString("outputValue", "");
                text = (BitmapText) capsule.readSavable("text", null);
                entity = (ITextual) capsule.readSavable("entity", null);
            }

        }
        final AbstractControl control = new TextControl(this, textName, rowNum);
    }

    String outputValue();

    boolean isEnded();

}