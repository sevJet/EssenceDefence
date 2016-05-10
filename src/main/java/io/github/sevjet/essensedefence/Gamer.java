package io.github.sevjet.essensedefence;

import com.jme3.font.BitmapText;
import io.github.sevjet.essensedefence.util.Creator;

public class Gamer {
    private final String name = "Gold of player:";
    private final BitmapText text = Creator.text(name, 0, Configuration.getSettings().getHeight() - 30);
    protected int gold = 0;

    public Gamer() {
        this(0);
    }

    public Gamer(int gold) {
        this.gold = gold;

        update();
    }

    public int getGold() {
        return gold;
    }

    @Deprecated
    public void setGold(int gold) {
        this.gold = gold;

        update();
    }

    public boolean decGold(int gold) {
        if (this.gold - gold < 0)
            return false;
        this.gold -= gold;

        update();
        return true;
    }

    public int incGold(int gold) {
        return this.gold += gold;
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
