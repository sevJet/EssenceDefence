package io.github.sevjet.essensedefence.entity.building;

import com.jme3.math.ColorRGBA;
import io.github.sevjet.essensedefence.util.BoxSize;

import static io.github.sevjet.essensedefence.util.Creator.myWireframe;

public class Wireframe extends Building {

    public Wireframe(BoxSize size) {
        super(size, -1f);

        initGeometry();
    }

    private void initGeometry() {
        geometry = myWireframe(getSize(), "wireframe");

        geometry.setUserData("entity", this);
    }

    public void setCanBuild(boolean canBuild) {
        geometry.getMaterial().setColor("Color", canBuild ? ColorRGBA.Green : ColorRGBA.Red);
        update();
    }

}
