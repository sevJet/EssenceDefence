package io.github.sevjet.essensedefence.listener;


import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.AnalogListener;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.MapCell;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class CellListener implements AnalogListener {

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MAPPING_MAKE_PASSABLE) ||
                name.equals(MAPPING_RESET)) {
            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
                Cell cell = getCell(results);
                Field field = cell.getField();
                //TODO change
                if (field != null && cell instanceof MapCell) {
                    switch (name) {
                        case MAPPING_RESET:
                            //TODO change on casting buildings
                            ((MapCell) cell).removeBuilding();
                        case MAPPING_MAKE_PASSABLE:
                            ((MapCell) cell).setPassably(name.equals(MAPPING_MAKE_PASSABLE));
                            break;
                    }
                }
            }
        }
    }

}
