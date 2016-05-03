package io.github.sevjet.essensedefence;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class Cell extends JME3Object {
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

    public Cell(int x, int y, Geometry geometry) {
        super(x, y, geometry);
    }

    public Cell(int x, int y, Building occupiedBy) {
        super(x, y);
        this.occupiedBy = occupiedBy;

        updateGeometry();
    }

    public Cell(int x, int y, Geometry geometry, Building occupiedBy) {
        super(x, y, geometry);
        this.occupiedBy = occupiedBy;

        updateGeometry();
    }

    public Cell(int x, int y, boolean passable) {
        super(x, y);
        this.passable = passable;

        updateGeometry();
    }

    public Cell(int x, int y, Geometry geometry, boolean passable) {
        super(x, y, geometry);
        this.passable = passable;

        updateGeometry();
    }

    public Cell(int x, int y, Geometry geometry, Building occupiedBy, boolean passable) {
        super(x, y, geometry);
        this.occupiedBy = occupiedBy;
        this.passable = passable;

        updateGeometry();
    }

    public Building getBuilding() {
        return occupiedBy;
    }

    public void setBuilding(Building building) {
        this.occupiedBy = building;
        occupiedBy.setX(x);
        occupiedBy.setY(y);

        updateGeometry();
    }

    public boolean isPassable() {
        return passable;
    }

    public boolean getPassability() {
        return passable;
    }

    public void setPassability(boolean passable) {
        this.passable = passable;

        updateGeometry();
    }

    @Override
    public boolean updateGeometry() {
        if (super.updateGeometry()) {
            getGeometry().getMaterial().setColor("Color",
                    new ColorRGBA(
                            (passable || occupiedBy != null ? 1 : 0),
                            (passable ? 1 : 0),
                            (passable && occupiedBy == null ? 1 : 0),
                            1));
            if (getGeometry().getParent() != null &&
                    getGeometry().getParent().getClass() == Field.class &&
                    occupiedBy != null)
                ((Field)this.getGeometry().getParent()).addObject(occupiedBy);
            return true;
        } else {
            return false;
        }
    }
}
