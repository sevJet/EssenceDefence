package io.github.sevjet.essencedefence.listener;


import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.AnalogListener;

import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.RayHelper;

public class CellListener implements AnalogListener {

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(ListenerManager.MAPPING_MAKE_PASSABLE) ||
                name.equals(ListenerManager.MAPPING_RESET)) {
            CollisionResults results;
            results = RayHelper.rayCasting();

            if (results.size() > 0) {
                MapCell cell = (MapCell) RayHelper.getCell(results);
                MapField field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case ListenerManager.MAPPING_RESET:
                            //TODO change on casting buildings
                            if (cell.hasContent()) {
                                cell.removeBuilding();
                                break;
                            }
                        case ListenerManager.MAPPING_MAKE_PASSABLE:
                            cell.setPassably(name.equals(ListenerManager.MAPPING_MAKE_PASSABLE));
                            break;
                    }
                }
            }
        }
    }

}
