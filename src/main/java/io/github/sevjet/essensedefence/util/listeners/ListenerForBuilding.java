package io.github.sevjet.essensedefence.util.listeners;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.*;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;

import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.*;


public class ListenerForBuilding implements ActionListener {

    private Building building = null;

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

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
                if (isPressed) {
                    building = choiceBuilding(name);
                    Cell cell = getCell(results);

                    preBuildAppearance(building.getGeometry());
                    building.getGeometry().addControl(new PreBuildControl());

                    if (cell.getBuilding() == null)
                        cell.build(building);
                    else
                        cell.getField().build(0, 0, building);
                } else {
                    Cell cell = getCell(results);
                    Field field = cell.getField();

                    field.removeObject(building);
                    //TODO change
                    if (field != null) {
                        cell.build(choiceBuilding(name));
                        switch (name) {
                            case MAPPING_BUILD_WALL:
                                break;
                            case MAPPING_BUILD_TOWER:
                                if (cell.getBuilding() instanceof Tower)
                                    ((Tower) cell.getBuilding()).putCore(new Essence(1, 5, 1, 1, 0));
                                break;
                            case MAPPING_BUILD_PORTAL:
                                break;
                            case MAPPING_BUILD_FORTRESS:
                                if (cell.getBuilding() instanceof Fortress)
                                    ((Fortress) cell.getBuilding()).setHealth(100);
                                break;
                        }
                    }
                }

            }
        }
    }

    private Geometry preBuildAppearance(Geometry geom) {
        geom.getMaterial().getAdditionalRenderState().setWireframe(true);
        geom.getMaterial().setColor("Color", ColorRGBA.Yellow);
        geom.getMesh().setLineWidth(5f);
        return geom;
    }

    private class PreBuildControl extends AbstractControl {
        CollisionResults results;

        @Override
        protected void controlUpdate(float tpf) {
            results = rayCasting();
            if (results.size() > 0) {
                Cell cell = getCell(results);
                Field field = cell.getField();

                if (getSpatial().getUserData("entity") instanceof Building) {
                    Building build = (Building) getSpatial().getUserData("entity");
                    field.removeObject(build);
                    if (field.enoughPlaceFor(cell, build)) {
                        cell.build(build);
                    } else
                        field.build(build.getX(), build.getY(), build);
                }
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {

        }
    }
}
