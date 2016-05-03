package io.github.sevjet.essensedefence;

import com.jme3.scene.Geometry;

public class jme3Object {
    protected int x = 0;
    protected int y = 0;
    protected static final int z = 0;
    protected Geometry geometry = null;

    public jme3Object() {
        this.x = 0;
        this.y = 0;
        this.geometry = null;
    }

    public jme3Object(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public jme3Object(Geometry geometry) {
        this.geometry = geometry;
    }

    public jme3Object(int x, int y, Geometry geometry) {
        this.x = x;
        this.y = y;
        this.geometry = geometry;
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

    public boolean updateGeometry() {
        if (this.geometry != null) {
            this.geometry.setLocalTranslation(x, y, z);
            return true;
        } else return false;
    }
}
