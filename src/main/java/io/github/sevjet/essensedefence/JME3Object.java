package io.github.sevjet.essensedefence;

import com.jme3.export.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.io.IOException;
import java.io.Serializable;


public abstract class JME3Object implements Savable {//implements Serializable {

    protected static final int z = 0;
    protected int x = 0;
    protected int y = 0;

    protected transient Geometry geometry = null;

    public JME3Object() {
        this(0, 0);
    }

    public JME3Object(int x, int y) {
        this.x = x;
        this.y = y;

        this.geometry = GeometryManager.getDefault(this.getClass());
        if (this.geometry == null) {
            this.geometry = GeometryManager.getDefault(JME3Object.class);
        }

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

    @Deprecated
    public void setGeometry(Geometry geometry) {
        if (geometry == null) {
            throw new IllegalArgumentException("Geometry can not be null");
        }
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

    protected boolean updater() {
        if (this.geometry == null) {
            this.geometry = GeometryManager.getDefault(JME3Object.class);
        }
        if (geometry != null) {
            geometry.setLocalTranslation(x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
        capsule.write(geometry, "geometry", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        x = capsule.readInt("x", 0);
        y = capsule.readInt("y", 0);
        geometry = (Geometry) capsule.readSavable("geometry", null);
    }
}
