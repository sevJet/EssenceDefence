package io.github.sevjet.essensedefence.listener;


import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.AnalogListener;
import io.github.sevjet.essensedefence.field.MapCell;
import io.github.sevjet.essensedefence.field.MapField;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class CellListener implements AnalogListener {

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MAPPING_MAKE_PASSABLE) ||
                name.equals(MAPPING_RESET)) {
            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
                MapCell cell = (MapCell) getCell(results);
                MapField field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_RESET:
                            //TODO change on casting buildings
                            if (cell.hasContent()) {
                                cell.removeBuilding();
                                break;
                            }
                        case MAPPING_MAKE_PASSABLE:
                            cell.setPassably(name.equals(MAPPING_MAKE_PASSABLE));
                            break;
                    }
                }
            }
        }
    }

}
