package io.github.sevjet.essencedefence.listener;


import com.jme3.input.controls.AnalogListener;

import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.RayHelper;

public class CellListener implements AnalogListener {

    private float delay = 0f;

    @Override
    public void onAnalog(String name, float value, float tpf) {
        delay -= tpf;
        if (delay > 0f) {
            return;
        }
        delay = 0f;

        if (name.equals(ListenerManager.MAPPING_MAKE_PASSABLE) ||
                name.equals(ListenerManager.MAPPING_RESET)) {
            Entity entity = RayHelper.collideClosest(RayHelper.getMapField());
            if (entity != null) {
                MapCell cell = (MapCell) entity;
                MapField field = cell.getField();

                if (field != null) {
                    switch (name) {
                        case ListenerManager.MAPPING_RESET:
                            if (cell.hasContent()) {
                                cell.removeBuilding();
                                delay += 0.5f;
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
