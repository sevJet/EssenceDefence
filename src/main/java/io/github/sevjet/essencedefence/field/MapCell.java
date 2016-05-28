package io.github.sevjet.essencedefence.field;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import io.github.sevjet.essencedefence.entity.building.Building;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;

import java.io.IOException;

public class MapCell extends Cell<Building> {

    protected boolean passable = false;
    protected boolean passableBuilding = true;

    public MapCell() {
        this(0, 0, false, null);
    }

    public MapCell(final int x, final int y) {
        this(x, y, false, null);
    }

    public MapCell(final int x, final int y, final boolean passable, final Building content) {
        super(x, y, content);
        this.passable = passable;

        update();
    }

    @Deprecated
    public Building getBuilding() {
        return getContent();
    }

    @Deprecated
    public void setBuilding(Building building) {
        setContent(building);

        update();
    }

    @Deprecated
    public boolean isOccupied() {
        return hasContent();
    }

    @Override
    public void setContent(Building content) {
        super.setContent(content);

        passableBuilding = content == null ||
                content instanceof Portal ||
                content instanceof Fortress;
    }

    public boolean build(final Building building) {
        MapField field = getField();
        if (field != null) {
            if (field.enoughPlaceFor(this, building)) {
                field.build(x, y, building);
                return true;
            }
        }
        return false;
    }

    @Override
    public MapField getField() {
        Field field = super.getField();
        if (field instanceof MapField) {
            return (MapField) field;
        }
        return null;
    }

    public boolean isPassable() {
        return content == null ? passable : passableBuilding;
    }

    public void setPassably(final boolean passable) {
        this.passable = passable;


        update();
    }

    @Override
    protected boolean update() {
        if (super.update()) {
            getGeometry().getMaterial().setColor("Color",
                    new ColorRGBA(
                            (passable || hasContent() ? 1 : 0),
                            (passable ? 1 : 0),
                            (passable && !hasContent() ? 1 : 0),
                            1));
            return true;
        }
        return false;
    }

    @Deprecated
    public void removeBuilding() {
        if (!hasContent())
            return;
        Field field = getField();
        if (field != null) {
            field.removeObject(getContent());
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(passable, "passable", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        passable = capsule.readBoolean("passable", false);
    }

}
