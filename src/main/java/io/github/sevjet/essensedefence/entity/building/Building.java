package io.github.sevjet.essensedefence.entity.building;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import io.github.sevjet.essensedefence.entity.Entity3D;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.MapCell;
import io.github.sevjet.essensedefence.field.MapField;
import io.github.sevjet.essensedefence.util.BoxSize;

import java.io.IOException;
import java.util.Map;

public abstract class Building extends Entity3D {

    protected float health = -1f;

    private boolean destroyed = false;

    public Building() {
        super();
    }

    public Building(BoxSize size, float health) {
        super(size);
        setHealth(health);
    }

    public Building(int x, int y, BoxSize size, float health) {
        super(x, y, size);
        setHealth(health);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        if (health <= 0f && health != -1f) {
            throw new IllegalArgumentException("Health can not be negative. For infinite use -1f");
        }
        this.health = health;
    }

    public void build() {
        MapField field = getField();
        if (field != null) {
            for(int i=getX();i<getX()+getSize().getWidth();i++) {
                for(int j=getY();j<getY()+getSize().getHeight();j++) {
                    final MapCell cell = field.getCell(i, j);
                    if(cell != null) {
                        cell.setContent(this);
                    }
                }
            }
        }
    }

    public MapField getField() {
        final Field field = super.getField();
        return field != null ? (MapField) field : null;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(health, "health", -1f);
        capsule.write(destroyed, "destroyed", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        health = capsule.readFloat("health", -1f);
        destroyed = capsule.readBoolean("destroyed", false);
    }

}
