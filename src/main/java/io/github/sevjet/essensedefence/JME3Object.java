package io.github.sevjet.essensedefence;

import com.jme3.scene.Geometry;

import java.io.Serializable;

public class JME3Object implements Serializable{

    protected static final int z = 0;
    protected int x = 0;
    protected int y = 0;
    protected transient Geometry geometry = null;

    public JME3Object() {
        this(0, 0, null);
    }

    public JME3Object(int x, int y) {
        this(x, y, null);
    }

    public JME3Object(Geometry geometry) {
        this(0, 0, geometry);
    }

    public JME3Object(int x, int y, Geometry geometry) {
        this.x = x;
        this.y = y;
        this.geometry = (geometry != null) ? geometry : GeometryManager.getDefault(this.getClass());

        updater();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;

        updater();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;

        updater();
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;

        updater();
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;

        updater();
    }

    public void moveRelative(int x, int y) {
        this.x += x;
        this.y += y;

        updater();
    }

    public boolean updater() {
        if (geometry != null) {
            geometry.setLocalTranslation(x, y, z);
            return true;
        } else {
            return false;
        }
    }

}
