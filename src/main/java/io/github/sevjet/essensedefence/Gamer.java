package io.github.sevjet.essensedefence;

import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

public class Gamer {
    protected int gold;

    // TODO: 09/05/2016 change
    {
        BitmapText text;
        text = Creator.text("name", "Gold of player:", 0, Configuration.getSettings().getHeight() - 30);
        text.addControl(new AbstractControl() {
            String name;

            {
                name = text.getText();
            }

            @Override
            protected void controlUpdate(float tpf) {
                text.setText(name + " " + gold);
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {

            }
        });
    }
}
