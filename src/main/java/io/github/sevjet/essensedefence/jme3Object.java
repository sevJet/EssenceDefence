package io.github.sevjet.essensedefence;

import com.jme3.scene.Geometry;

public class jme3Object {
    protected int x;
    protected int y;
    protected Geometry geometry;

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
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
