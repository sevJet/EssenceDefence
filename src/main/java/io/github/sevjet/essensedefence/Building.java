package io.github.sevjet.essensedefence;

import com.jme3.scene.shape.Box;

public abstract class Building extends JME3Object {

    protected int height;
    protected int width;

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public boolean updater() {
        if (geometry != null) {
            float zOffset = 0;
            if (geometry.getMesh() instanceof Box) {
                zOffset = ((Box) geometry.getMesh()).zExtent;
            }
            geometry.setLocalTranslation(x + (width - 1) / 2.0F, y + (height - 1) / 2.0F, z + zOffset);
            return true;
        } else {
            return false;
        }
    }

}
