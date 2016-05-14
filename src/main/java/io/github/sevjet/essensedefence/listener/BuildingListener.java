package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.*;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;


public class BuildingListener implements ActionListener {

    private Building wireframe = null;
    private String currentMap = "";

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

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
                Cell cell = getCell(results);
                Field field = cell.getField();

                if (isPressed) {
                    if (currentMap.isEmpty() || currentMap.equals(name)) {
                        currentMap = name;

                        wireframe = new Wireframe(getBoxSize(name));

                        wireframe.getGeometry().addControl(new PreBuildControl());

                        wireframe.setX(cell.getX());
                        wireframe.setY(cell.getY());
                        field.removeAll(Wireframe.class);
                        field.addObject(wireframe);
                    }
                } else {
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
        }
    }

    private class PreBuildControl extends AbstractControl {
        CollisionResults results;

        @Override
        protected void controlUpdate(float tpf) {
            results = rayCasting();
            if (results.size() > 0) {
                Cell cell = getCell(results);
                Field field = cell.getField();

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
}
