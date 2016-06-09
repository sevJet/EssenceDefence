package io.github.sevjet.essencedefence.listener;

import com.jme3.input.controls.ActionListener;

import io.github.sevjet.essencedefence.control.WireframeControl;
import io.github.sevjet.essencedefence.entity.Entity;
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
import io.github.sevjet.essencedefence.util.RayHelper;

public class BuildingListener implements ActionListener {

    private String currentMap = "";
    private MapCell cell;
    private MapField field;
    private Wireframe wireframe;

    private Building choiceBuilding(String name) {
        switch (name) {
            case ListenerManager.MAPPING_BUILD_WALL:
                return new Wall();
            case ListenerManager.MAPPING_BUILD_TOWER:
                return new Tower();
            case ListenerManager.MAPPING_BUILD_PORTAL:
                return new Portal();
            case ListenerManager.MAPPING_BUILD_FORTRESS:
                return new Fortress();
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

    private void initBuilding(String name, Building building) {
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

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_BUILD_WALL) ||
                name.equals(ListenerManager.MAPPING_BUILD_TOWER) ||
                name.equals(ListenerManager.MAPPING_BUILD_PORTAL) ||
                name.equals(ListenerManager.MAPPING_BUILD_FORTRESS)) {

            Entity closest = RayHelper.collideClosest(RayHelper.getMapField());
            if (closest != null) {
                cell = (MapCell) closest;
                field = cell.getField();

                if (isPressed) {
                    onPress(name);
                } else {
                    onRelease(name);
                }

            }
        }
    }

    private void onPress(String name) {
        if (currentMap.isEmpty()) {
            currentMap = name;
        }
        if (currentMap.equals(name)) {
            field.removeAll(Wireframe.class);

            wireframe = new Wireframe(getBuildingClass(name), getBoxSize(name));
            wireframe.getGeometry().addControl(new WireframeControl());
            wireframe.move(cell.getX(), cell.getY());

            field.addObject(wireframe);
        }
    }

    private void onRelease(String name) {
        if (currentMap.equals(name) && field != null && !wireframe.isDestroyed()) {
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
                    initBuilding(name, building);
                }
            }
            currentMap = "";
        }

    }
}