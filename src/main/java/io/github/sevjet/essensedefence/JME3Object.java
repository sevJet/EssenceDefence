package io.github.sevjet.essensedefence;

import com.jme3.scene.Geometry;

public class JME3Object {

    protected int x = 0;
    protected int y = 0;
    protected static final int z = 0;
    protected Geometry geometry = null;

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

        updateGeometry();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;

        updateGeometry();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;

        updateGeometry();
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;

        updateGeometry();
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;

        updateGeometry();
    }

    public void moveRelative(int x, int y) {
        this.x += x;
        this.y += y;

        updateGeometry();
    }

    public boolean updateGeometry() {
        if(geometry != null) {
            geometry.setLocalTranslation(x, y, z);
            return true;
        } else {
            return false;
        }
    }

}
