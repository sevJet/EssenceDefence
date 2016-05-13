package io.github.sevjet.essensedefence.util.listeners;


import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.MAPPING_MAKE_PASSABLE;
import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.MAPPING_RESET;
import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.rayCasting;

public class ListenerForCell implements AnalogListener {

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MAPPING_MAKE_PASSABLE) ||
                name.equals(MAPPING_RESET)) {
            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
                Cell temp = new Cell();
                Geometry target = results.getClosestCollision().getGeometry();
                Cell cell = ((Field) target.getParent().getParent()).getCell(target);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_RESET:
                            //TODO change on casting buildings
                            cell.removeBuilding();
                        case MAPPING_MAKE_PASSABLE:
                            cell.setPassably(name.equals(MAPPING_MAKE_PASSABLE));
                            break;
                    }
                }
            }
        }
    }

}
