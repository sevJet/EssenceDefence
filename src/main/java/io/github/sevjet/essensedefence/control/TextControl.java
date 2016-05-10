package io.github.sevjet.essensedefence.control;

import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.Gamer;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

public class TextControl extends AbstractControl {

    private String textContent;
    private BitmapText text;
    private Object entity;

    public TextControl(Object object) {
//        object.getGeometry().setUserData("entity", object);
//        System.out.println("--- " + Configuration.getRootNode().getNumControls());
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
//        if (entity == null) {
//            Configuration.getRootNode().detachChild(text);
//            text.setText(" -  delete -");
//            System.out.println(Configuration.getRootNode().removeControl(this));
//            return;
//        }
        if (entity instanceof Fortress) {
            Fortress fort = ((Fortress) entity);
            text.setText(textContent + " " + fort.getHealth());
            if (fort.getHealth() < 0){
//                System.out.println(" -- delete TextControl");
                Configuration.getGuiNode().detachChild(text);
                Configuration.getGuiNode().removeControl(this);
            }
            return;
        }
        if (entity instanceof Gamer) {
            Gamer gamer = ((Gamer) entity);
            text.setText(textContent + " " + gamer.getGold());
            if (gamer .getGold() < 0){
//                System.out.println(" -- delete TextControl");
                Configuration.getGuiNode().detachChild(text);
                Configuration.getGuiNode().removeControl(this);
            }
            return;
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
