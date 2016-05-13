package io.github.sevjet.essensedefence.util.listeners;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;

import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.*;

public class ListenerForBuilding implements ActionListener {

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0 && !isPressed) {
                Cell cell = getCell(results);
                Field field = cell.getField();
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
