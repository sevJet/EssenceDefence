package io.github.sevjet.essensedefence;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

import java.io.Serializable;

public class Cell extends JME3Object implements Serializable {
    protected Building occupiedBy = null;
    protected boolean passable = false;

    public Cell() {
        super();

        this.occupiedBy = null;
        this.passable = false;
    }

    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell(int x, int y, Building occupiedBy) {
        super(x, y);
        this.occupiedBy = occupiedBy;

        updater();
    }

    public Cell(int x, int y, boolean passable) {
        super(x, y);
        this.passable = passable;

        updater();
    }

    @Deprecated
    public Cell(int x, int y, Geometry geometry) {
        super(x, y, geometry);
    }

    @Deprecated
    public Cell(int x, int y, Geometry geometry, Building occupiedBy) {
        super(x, y, geometry);
        this.occupiedBy = occupiedBy;

        updater();
    }

    @Deprecated
    public Cell(int x, int y, Geometry geometry, boolean passable) {
        super(x, y, geometry);
        this.passable = passable;

        updater();
    }

    @Deprecated
    public Cell(int x, int y, Geometry geometry, Building occupiedBy, boolean passable) {
        super(x, y, geometry);
        this.occupiedBy = occupiedBy;
        this.passable = passable;

        updater();
    }

    public Building getBuilding() {
        return occupiedBy;
    }

    public void setBuilding(Building building) {
        this.occupiedBy = building;

        updater();
    }

    public boolean isPassable() {
        return passable;
    }

    public boolean getPassability() {
        return passable;
    }

    public void setPassably(boolean passable) {
        this.passable = passable;

        updater();
    }

    @Override
    protected boolean updater() {
        if (super.updater()) {
            getGeometry().getMaterial().setColor("Color",
                    new ColorRGBA(
                            (passable || occupiedBy != null ? 1 : 0),
                            (passable ? 1 : 0),
                            (passable && occupiedBy == null ? 1 : 0),
                            1));
            return true;
        }
        return false;
    }
}