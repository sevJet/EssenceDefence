package io.github.sevjet.essensedefence.entity.building;

import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;


public class Fortress extends Building {

    private static final BoxSize SIZE = new BoxSize(3, 3, 4);

    // FIXME: 10/05/2016 make final
    private String name;
    private BitmapText text;

    public Fortress() {
        super(SIZE, -1f);
    }

    public Fortress(float health) {
        super(SIZE, health);
    }

    public Fortress(int x, int y, float health) {
        super(x, y, SIZE, health);
    }

    @Override
    protected boolean updater() {
        super.updater();
        if (text == null) {
            // FIXME: 10/05/2016
//            throw new IllegalArgumentException("bitmap text must be initialize");
            name = "Health of fortress:";
            text = Creator.text(name);
        }
        text.setText(name + " " + health);
        return true;
    }
}
