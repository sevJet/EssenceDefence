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
import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.getCell;


public class ListenerForBuilding implements ActionListener {

    private Building building = null;

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0 && isPressed) {
                switch (name){
                    case MAPPING_BUILD_WALL:
                        building = new Wall();
                        break;
                    case MAPPING_BUILD_TOWER:
                        building = new Tower();
                        break;
                    case MAPPING_BUILD_PORTAL:
                        building = new Portal();
                        break;
                    case MAPPING_BUILD_FORTRESS:
                        building = new Fortress();
                        break;
                }
                Cell cell = getCell(results);
                if (cell.getBuilding() == null)
                    cell.build(building);
                else cell.getField().build(0, 0, building);
                building.getGeometry().getMaterial().getAdditionalRenderState().setWireframe(true);
                building.getGeometry().getMaterial().setColor("Color", ColorRGBA.Yellow);
                building.getGeometry().getMesh().setLineWidth(5f);

                building.getGeometry().addControl(new AbstractControl() {
                    CollisionResults results2;

                    @Override
                    protected void controlUpdate(float tpf) {
                        results2 = rayCasting();
                        if (results2.size() > 0) {
                            Cell cell = getCell(results2);
                            Field field = cell.getField();

                            if (getSpatial().getUserData("entity") instanceof Building) {
                                Building build = (Building) getSpatial().getUserData("entity");
                                field.removeObject(build);
                                if (field.enoughPlaceFor(cell, build)){
                                    cell.build(build);
                                } else field.build(build.getX(), build.getY(), build);
                            }
                        }
                    }

                    @Override
                    protected void controlRender(RenderManager rm, ViewPort vp) {

                    }
                });
            }

            if (results.size() > 0 && !isPressed) {
                Cell cell = getCell(results);
                Field field = cell.getField();

                field.removeObject(building);
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_BUILD_WALL:
                            cell.build(new Wall());
                            break;
                        case MAPPING_BUILD_TOWER:
                            if (cell.build(new Tower()))
                                ((Tower) cell.getBuilding()).putCore(new Essence(1, 5, 1, 1, 0));
                            break;
                        case MAPPING_BUILD_PORTAL:
                            cell.build(new Portal());
                            break;
                        case MAPPING_BUILD_FORTRESS:
                            cell.build(new Fortress(100f));
                            break;
                    }
                }
            }
        }
    }
}
