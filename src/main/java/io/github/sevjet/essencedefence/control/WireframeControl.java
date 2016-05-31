package io.github.sevjet.essencedefence.control;

import com.jme3.collision.CollisionResults;

import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.building.Wireframe;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.PathBuilder;
import io.github.sevjet.essencedefence.util.RayHelper;

public class WireframeControl extends BasicControl {
    CollisionResults results;

    @Override
    protected void controlUpdate(float tpf) {
        results = RayHelper.rayCasting();
        if (results.size() > 0) {
            MapCell cell = (MapCell) RayHelper.getCell(results);
            MapField field = cell.getField();

            if (getSpatial().getUserData("entity") instanceof Wireframe) {
                Wireframe wireframe = getSpatial().getUserData("entity");
                wireframe.move(cell.getX(), cell.getY());
                boolean canBuild = false;
                if (field.enoughPlaceFor(cell, wireframe)) {
                    canBuild = PathBuilder.atField(field)
                            .withBuilding(wireframe.getBuildingClass())
                            .withSize(wireframe.getSize())
                            .atPoint(cell.getX(), cell.getY())
                            .from(Portal.class)
                            .andFrom(Monster.class)
                            .to(Fortress.class)
                            .isValid();
                }
                wireframe.setCanBuild(canBuild);
            }
        } else {
            spatial.removeFromParent();
            spatial.removeControl(this);
        }
    }
}