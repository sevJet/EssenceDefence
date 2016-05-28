package io.github.sevjet.essencedefence.entity;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import java.io.IOException;

import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.util.GeometryManager;

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

        update();
    }

    public int getX() {
        return x;
    }

    @Deprecated
    public void setX(int x) {
        this.x = x;

        update();
    }

    public int getY() {
        return y;
    }

    @Deprecated
    public void setY(int y) {
        this.y = y;

        update();
    }

    public int getZ() {
        return z;
    }

    @Deprecated
    public void setZ(int z) {
        this.z = z;

        update();
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
        update();
    }

    public void translate(int x, int y) {
        translate(x, y, 0);
    }

    public void translate(int x, int y, int z) {
        translate(x, y, z, true);
    }

    public void translate(int x, int y, int z, boolean doUpdate) {
        move(this.x + x, this.y + y, this.z + z, doUpdate);
    }

    public void move(int x, int y) {
        move(x, y, this.z);
    }

    public void move(int x, int y, int z) {
        move(x, y, z, true);
    }

    public void move(int x, int y, int z, boolean doUpdate) {
        this.x = x;
        this.y = y;
        this.z = z;

        if (doUpdate) {
            update();
        }
    }

    public Field getField() {
        if (geometry != null &&
                geometry.getParent() != null &&
                geometry.getParent().getParent() != null) {
            if (geometry.getParent().getParent() instanceof Field) {
                return (Field) geometry.getParent().getParent();
            }
//            if (geometry.getParent().getParent() instanceof Inventory){
//                return (Inventory) geometry.getParent().getParent();
//            }
        }
        return null;
    }

    public Vector3f getCenter() {
        return new Vector3f(x, y, z);
    }

    public Vector3f getPhysicalCenter() {
        return geometry == null ?
                Vector3f.ZERO :
                geometry.getLocalTranslation();
    }

    protected boolean moveToCenter() {
        geometry.setLocalTranslation(getCenter());
        return true;
    }

    protected boolean update() {
        return geometry != null && moveToCenter();
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
