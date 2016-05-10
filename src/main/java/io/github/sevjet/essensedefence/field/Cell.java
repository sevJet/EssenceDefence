package io.github.sevjet.essensedefence.field;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.building.Building;
import io.github.sevjet.essensedefence.util.Configuration;

import java.io.IOException;

public class Cell extends Entity {

    protected Building occupiedBy = null;
    protected boolean passable = false;

    public Cell() {
        this(0, 0, false, null);
    }

    public Cell(int x, int y) {
        this(x, y, false, null);
    }

    public Cell(int x, int y, boolean passable, Building occupiedBy) {
        super(x, y);
        this.passable = passable;
        this.occupiedBy = occupiedBy;

        updater();
    }

    public Building getBuilding() {
        return occupiedBy;
    }

    public void setBuilding(Building building) {
        this.occupiedBy = building;

        updater();
    }

    public boolean build(Building building) {
        Field field = getField();
        if (field != null) {
            if (field.enoughPlaceFor(this, building)) {
                field.build(x, y, building);
                return true;
            }
        }
        return false;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassably(boolean passable) {
        this.passable = passable;

        updater();
    }
    //TODO 
    @Override
    protected boolean updater() {
        Texture tex;
        super.updater();
            if(passable) {
                tex = Configuration.getAssetManager().loadTexture("textures/path.jpg");
            }
            else {
                tex = Configuration.getAssetManager().loadTexture("textures/earth.jpg");
            }
            getGeometry().getMaterial().setTexture("ColorMap",tex);
//
//                    new ColorRGBA(
//                            (passable || occupiedBy != null ? 1 : 0),
//                            (passable ? 1 : 0),
//                            (passable && occupiedBy == null ? 1 : 0),
//                            1));
            return true;
        //}
        //return false;
    }

    public void removeBuilding() {
        if (occupiedBy == null)
            return;
        Field field = getField();
        if (field != null) {
            field.removeBuilding(occupiedBy);
        }
    }

    protected void free() {
        occupiedBy = null;
        updater();
    }

    public Field getField() {
        if (geometry != null &&
                geometry.getParent() != null &&
                geometry.getParent().getParent() != null &&
                geometry.getParent().getParent() instanceof Field) {
            return ((Field) geometry.getParent().getParent());
        }
        return null;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(occupiedBy, "occupiedBy", null);
        capsule.write(passable, "passable", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        occupiedBy = (Building) capsule.readSavable("occupiedBy", null);
        passable = capsule.readBoolean("passable", false);
    }
}