package io.github.sevjet.essensedefence.entity.building;

import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

import static java.awt.SystemColor.text;


public class Fortress extends Building {

    private static final BoxSize SIZE = new BoxSize(3, 3, 4);

    // TODO: 09/05/2016 change
    {
        BitmapText text;
//        text = Creator.text("name", "Health fortress:", 0, (int)Configuration.getSettings().getHeight());
        text = Creator.text("Health of fortress:", 0,
                Configuration.getSettings().getHeight());
        text.addControl(new AbstractControl() {
            String name;

            {
                name = text.getText();
            }

            @Override
            protected void controlUpdate(float tpf) {
                text.setText(name + " " + health);
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {

            }
        });
    }

    public Fortress() {super(SIZE, -1f);}

    public Fortress(float health) {
        super(SIZE, health);
    }

    public Fortress(int x, int y, float health) {
        super(x, y, SIZE, health);
    }

}
