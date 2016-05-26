package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.building.*;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.MapCell;
import io.github.sevjet.essensedefence.field.MapField;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.PathBuilder;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;


public class BuildingListener implements ActionListener {

    private String currentMap = "";
    private MapCell cell;
    private MapField field;
    private Wireframe wireframe;

    private Building choiceBuilding(String name) {
        switch (name) {
            case MAPPING_BUILD_WALL:
                return new Wall();
//            break;
            case MAPPING_BUILD_TOWER:
                return new Tower();
//            break;
            case MAPPING_BUILD_PORTAL:
                return new Portal();
//            break;
            case MAPPING_BUILD_FORTRESS:
                return new Fortress();
//            break;
        }
        return null;
    }

    private BoxSize getBoxSize(String name) {
        switch (name) {
            case MAPPING_BUILD_WALL:
                return Wall.SIZE;
            case MAPPING_BUILD_TOWER:
                return Tower.SIZE;
            case MAPPING_BUILD_PORTAL:
                return Portal.SIZE;
            case MAPPING_BUILD_FORTRESS:
                return Fortress.SIZE;
        }
        return BoxSize.FLAT;
    }

    private Class<? extends Building> getBuildingClass(String name) {
        switch (name) {
            case MAPPING_BUILD_WALL:
                return Wall.class;
            case MAPPING_BUILD_TOWER:
                return Tower.class;
            case MAPPING_BUILD_PORTAL:
                return Portal.class;
            case MAPPING_BUILD_FORTRESS:
                return Fortress.class;
        }
        return Building.class;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS)) {

            CollisionResults results = rayCasting();

            if (results.size() > 0) {
                cell = (MapCell) getCell(results);
                field = cell.getField();

                if (isPressed) {
                    onPress(name, tpf);
                } else {
                    onRelease(name, tpf);
                }

            }
        }
    }

    private void onPress(String name, float tpf) {
        if (currentMap.isEmpty() || currentMap.equals(name)) {
            currentMap = name;

            wireframe = new Wireframe(getBuildingClass(name), getBoxSize(name));

            wireframe.getGeometry().addControl(new PreBuildControl());

            wireframe.move(cell.getX(), cell.getY());
            field.removeAll(Wireframe.class);
            field.addObject(wireframe);
        }
    }

    private void onRelease(String name, float tpf) {
        if (currentMap.equals(name) && field != null) {
            field.removeAll(Wireframe.class);
            Building building = choiceBuilding(name);
            if (field.enoughPlaceFor(cell, building)) {
                if (PathBuilder.atField(field)
                        .withBuilding(wireframe.getBuildingClass())
                        .withSize(wireframe.getSize())
                        .atPoint(cell.getX(), cell.getY())
                        .from(Portal.class)
                        .andFrom(Monster.class)
                        .to(Fortress.class)
                        .isValid()) {
                    cell.build(building);
                    switch (name) {
                        case MAPPING_BUILD_WALL:
                            break;
                        case MAPPING_BUILD_TOWER:
                            break;
                        case MAPPING_BUILD_PORTAL:
                            break;
                        case MAPPING_BUILD_FORTRESS:
                            if (building instanceof Fortress)
                                building.setHealth(100);
                            break;
                    }
                }
            }
            currentMap = "";
        }

    }
}

// TODO: 14/05/2016 refactor this
class PreBuildControl extends AbstractControl {
    CollisionResults results;

    @Override
    protected void controlUpdate(float tpf) {
        results = rayCasting();
        if (results.size() > 0) {
            MapCell cell = (MapCell) getCell(results);
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
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}