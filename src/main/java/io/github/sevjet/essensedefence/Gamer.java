package io.github.sevjet.essensedefence;

import com.jme3.font.BitmapText;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.control.TextControl;
import io.github.sevjet.essensedefence.util.Creator;

public class Gamer {
//    private final String name = "Gold of player:";
//    private final BitmapText text = Creator.text(name, 1);

    private final AbstractControl control = new TextControl(this, "Gold of gamer:", 1);
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

//    protected void changeText(){
//        if (text == null) {
//            throw new IllegalArgumentException("bitmap text must be initialize");
//        }
//        text.setText(name + " " + gold);
//    }
    protected boolean update() {
//        changeText();
        return true;
    }

}
