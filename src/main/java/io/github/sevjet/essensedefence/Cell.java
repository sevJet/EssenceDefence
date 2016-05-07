package io.github.sevjet.essensedefence;

import com.jme3.math.ColorRGBA;

import java.io.Serializable;

public class Cell extends JME3Object implements Serializable {
    protected Building occupiedBy = null;
    protected boolean passable = false;

    public Cell() {
        this(0, 0, false, null);
    }

    public Cell(int x, int y) {
        this(x, y, false, null);
    }

    public Cell(int x, int y, Building occupiedBy) {
        this(x, y, false, occupiedBy);
    }

    public Cell(int x, int y, boolean passable) {
        this(x, y, passable, null);
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
        if (getGeometry().getParent().getParent() instanceof Field) {
            Field field = ((Field) getGeometry().getParent().getParent());
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

    public void removeBuilding() {
        if (occupiedBy == null)
            return;
        if (geometry.getParent().getParent() instanceof Field) {
            ((Field) geometry.getParent().getParent()).removeBuilding(occupiedBy);
        }
    }

    protected void free() {
        occupiedBy = null;
        updater();
    }
}