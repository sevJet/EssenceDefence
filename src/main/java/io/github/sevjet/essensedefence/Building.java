package io.github.sevjet.essensedefence;

import com.jme3.scene.shape.Box;

import java.io.Serializable;

public abstract class Building extends JME3Object implements Serializable {

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

    @Override
    public boolean updater() {
        if (geometry != null) {
            float zOffset = 0;
            if (geometry.getMesh() instanceof Box) {
                zOffset = ((Box) geometry.getMesh()).zExtent;
            }
            geometry.setLocalTranslation(x + (width - 1) / 2.0F, y + (height - 1) / 2.0F, z + zOffset);
            return true;
        } else {
            geometry = GeometryManager.getDefault(this.getClass());
            if (geometry == null) {
                geometry = GeometryManager.getDefault(JME3Object.class);
            }
            //TODO dangerous recursion
            return updater();
        }
    }

}
