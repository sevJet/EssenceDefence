package io.github.sevjet.essensedefence;

import com.jme3.font.BitmapText;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

public class Gamer {
    protected int gold;

    private final String name = "Gold of player:";
    private final BitmapText text = Creator.text(name, 0, Configuration.getSettings().getHeight() - 30);

    public Gamer() {
    }

    protected boolean update() {
        if (text == null) {
            throw new IllegalArgumentException("bitmap text must be initialize");
        }
        text.setText(name + " " + gold);
        return true;
    }

    //    {
//        BitmapText text;
//        text = Creator.text("name", "Gold of player:", 0, Configuration.getSettings().getHeight() - 30);
//        text.addControl(new AbstractControl() {
//            String name;
//
//            {
//                name = text.getText();
//            }
//
//            @Override
//            protected void controlUpdate(float tpf) {
//                text.setText(name + " " + gold);
//            }
//
//            @Override
//            protected void controlRender(RenderManager rm, ViewPort vp) {
//
//            }
//        });
//              }
}
