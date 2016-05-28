package io.github.sevjet.essencedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

import io.github.sevjet.essencedefence.entity.building.Building;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.entity.building.Wall;
import io.github.sevjet.essencedefence.entity.building.Wireframe;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.BoxSize;
import io.github.sevjet.essencedefence.util.PathBuilder;


public class BuildingListener implements ActionListener {

    private String currentMap = "";
    private MapCell cell;
    private MapField field;
    private Wireframe wireframe;

    private Building choiceBuilding(String name) {
        switch (name) {
            case ListenerManager.MAPPING_BUILD_WALL:
                return new Wall();
//            break;
            case ListenerManager.MAPPING_BUILD_TOWER:
                return new Tower();
//            break;
            case ListenerManager.MAPPING_BUILD_PORTAL:
                return new Portal();
//            break;
            case ListenerManager.MAPPING_BUILD_FORTRESS:
                return new Fortress();
//            break;
        }
        return null;
    }

    private BoxSize getBoxSize(String name) {
        switch (name) {
            case ListenerManager.MAPPING_BUILD_WALL:
                return Wall.SIZE;
            case ListenerManager.MAPPING_BUILD_TOWER:
                return Tower.SIZE;
            case ListenerManager.MAPPING_BUILD_PORTAL:
                return Portal.SIZE;
            case ListenerManager.MAPPING_BUILD_FORTRESS:
                return Fortress.SIZE;
        }
        return BoxSize.FLAT;
    }

    private Class<? extends Building> getBuildingClass(String name) {
        switch (name) {
            case ListenerManager.MAPPING_BUILD_WALL:
                return Wall.class;
            case ListenerManager.MAPPING_BUILD_TOWER:
                return Tower.class;
            case ListenerManager.MAPPING_BUILD_PORTAL:
                return Portal.class;
            case ListenerManager.MAPPING_BUILD_FORTRESS:
                return Fortress.class;
        }
        return Building.class;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_BUILD_WALL) ||
                name.equals(ListenerManager.MAPPING_BUILD_TOWER) ||
                name.equals(ListenerManager.MAPPING_BUILD_PORTAL) ||
                name.equals(ListenerManager.MAPPING_BUILD_FORTRESS)) {

            CollisionResults results = ListenerManager.rayCasting();

            if (results.size() > 0) {
                cell = (MapCell) ListenerManager.getCell(results);
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
                        case ListenerManager.MAPPING_BUILD_WALL:
                            break;
                        case ListenerManager.MAPPING_BUILD_TOWER:
                            break;
                        case ListenerManager.MAPPING_BUILD_PORTAL:
                            break;
                        case ListenerManager.MAPPING_BUILD_FORTRESS:
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
        results = ListenerManager.rayCasting();
        if (results.size() > 0) {
            MapCell cell = (MapCell) ListenerManager.getCell(results);
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