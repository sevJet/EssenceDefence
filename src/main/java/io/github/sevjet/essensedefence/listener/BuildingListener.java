package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.*;
import io.github.sevjet.essensedefence.field.MapCell;
import io.github.sevjet.essensedefence.field.MapField;
import io.github.sevjet.essensedefence.util.BoxSize;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;


public class BuildingListener implements ActionListener {

    private Building wireframe = null;
    private String currentMap = "";
    private CollisionResults results;
    private MapCell cell;
    private MapField field;

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

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS)) {

            results = rayCasting();

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

            wireframe = new Wireframe(getBoxSize(name));

            wireframe.getGeometry().addControl(new PreBuildControl());

            wireframe.setX(cell.getX());
            wireframe.setY(cell.getY());
            field.removeAll(Wireframe.class);
            field.addObject(wireframe);
        }
    }

    private void onRelease(String name, float tpf) {
        if (currentMap.equals(name) && field != null) {
            field.removeAll(Wireframe.class);
            Building building = choiceBuilding(name);
            if (field.enoughPlaceFor(cell, building)) {
                cell.build(building);
                switch (name) {
                    case MAPPING_BUILD_WALL:
                        break;
                    case MAPPING_BUILD_TOWER:
                        if (building instanceof Tower)
                            ((Tower) building).putCore(new Essence(1, 5, 1, 1, 0));
                        break;
                    case MAPPING_BUILD_PORTAL:
                        break;
                    case MAPPING_BUILD_FORTRESS:
                        if (building instanceof Fortress)
                            building.setHealth(100);
                        break;
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
                wireframe.setX(cell.getX());
                wireframe.setY(cell.getY());
                wireframe.setCanBuild(field.enoughPlaceFor(cell, wireframe));
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}