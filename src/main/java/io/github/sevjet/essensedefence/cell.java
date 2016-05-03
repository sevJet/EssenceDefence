package io.github.sevjet.essensedefence;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class cell extends jme3Object {
    protected building occupiedBy = null;
    protected boolean passable = false;

    public cell() {
        super();
        this.occupiedBy = null;
        this.passable = false;
    }

    public cell(int x, int y) {
        super(x, y);
    }

    public cell(int x, int y, Geometry geometry) {
        super(x, y, geometry);
    }

    public cell(int x, int y, Geometry geometry, building occupiedBy) {
        super(x, y, geometry);
        this.occupiedBy = occupiedBy;
        updateGeometry();
    }

    public cell(int x, int y, boolean passable) {
        super(x, y);
        this.passable = passable;
        updateGeometry();
    }

    public cell(int x, int y, Geometry geometry, boolean passable) {
        super(x, y, geometry);
        this.passable = passable;
        updateGeometry();
    }

    public cell(int x, int y, Geometry geometry, building occupiedBy, boolean passable) {
        super(x, y, geometry);
        this.occupiedBy = occupiedBy;
        this.passable = passable;
        updateGeometry();
    }

    public building getBuild() {
        return occupiedBy;
    }

    public void setBuild(building build) {
        this.occupiedBy = build;
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
            this.getGeometry().getMaterial().setColor("Color",
                    new ColorRGBA(
                            (passable || occupiedBy != null ? 1 : 0),
                            (passable ? 1 : 0),
                            (passable && occupiedBy == null ? 1 : 0),
                            0));
            return true;
        } else return false;
    }
}
