package io.github.sevjet.essensedefence;

import java.io.Serializable;

public abstract class Building extends JME3Object implements Serializable {

    private int height;
    private int width;
    private int depth;

    public Building(int height, int width, int depth) {
        setSize(height, width, depth);
    }

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

    public int getDepth() {
        return depth;
    }

    protected void setDepth(int depth) {
        this.depth = depth;
    }

    protected void setSize(int height, int width, int depth) {
        this.height = height;
        this.width = width;
        this.depth = depth;
    }

    @Override
    protected boolean updater() {
        if (geometry == null) {
            geometry = GeometryManager.getDefault(this.getClass());
            if (geometry == null) {
                geometry = GeometryManager.getDefault(JME3Object.class);
            }
        }
        if (geometry != null) {
            geometry.setLocalTranslation(x + (width - 1) / 2.0F, y + (height - 1) / 2.0F, z + (depth) / 2.0F);
            return true;
        }
        return false;
    }

}
