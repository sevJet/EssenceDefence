package io.github.sevjet.essencedefence.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Spatial;

import java.io.IOException;

import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.building.Wireframe;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.PathBuilder;
import io.github.sevjet.essencedefence.util.RayHelper;

public class WireframeControl extends BasicControl {

    private Wireframe wireframe = null;

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (entity instanceof Wireframe) {
            wireframe = (Wireframe) entity;
        } else {
            throw new IllegalArgumentException("Not a Monster spatial");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        final Entity entity = RayHelper.collideClosest(RayHelper.getMapField());
        if (entity != null && entity instanceof MapCell) {
            final MapCell cell = (MapCell) entity;
            final MapField field = cell.getField();

            wireframe.move(cell.getX(), cell.getY());
            wireframe.setCanBuild(field.enoughPlaceFor(cell, wireframe) &&
                    PathBuilder.atField(field)
                            .withBuilding(wireframe.getBuildingClass())
                            .withSize(wireframe.getSize())
                            .atPoint(cell.getX(), cell.getY())
                            .from(Portal.class)
                            .andFrom(Monster.class)
                            .to(Fortress.class)
                            .isValid());
        } else {
            spatial.removeFromParent();
            spatial.removeControl(this);
            wireframe.destroy();
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(wireframe, "wireframe", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        wireframe = (Wireframe) capsule.readSavable("wireframe", null);
    }

}