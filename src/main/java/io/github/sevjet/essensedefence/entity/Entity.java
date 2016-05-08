package io.github.sevjet.essensedefence.entity;

import com.jme3.export.*;
import com.jme3.scene.Geometry;
import io.github.sevjet.essensedefence.util.GeometryManager;

import java.io.IOException;

public abstract class Entity implements Savable {

    protected int x = 0;
    protected int y = 0;
    protected int z = 0;

    protected Geometry geometry = null;

    public Entity() {
        this(0, 0);
    }

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;

        geometry = GeometryManager.getDefault(getClass());
        if (geometry == null) {
            geometry = GeometryManager.getDefault(Entity.class);
        }
        if (geometry != null) {
            geometry.setUserData("entity", this);
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

    protected boolean moveToCenter() {
        geometry.setLocalTranslation(x, y, z);
        return true;
    }

    protected boolean updater() {
        if (geometry != null) {
            return moveToCenter();
        }
        return false;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
        capsule.write(z, "z", 0);
        capsule.write(geometry, "geometry", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        x = capsule.readInt("x", 0);
        y = capsule.readInt("y", 0);
        z = capsule.readInt("z", 0);
        geometry = (Geometry) capsule.readSavable("geometry", null);
    }

}
