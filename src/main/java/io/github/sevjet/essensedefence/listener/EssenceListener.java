package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.gui.Inventory;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class EssenceListener implements ActionListener {
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUY_ESSENCE) ||
                name.equals(MAPPING_EXTRACTION_ESSENCE) ||
                name.equals(MAPPING_PUT_EXTRACTED_ESSENCE) ||
                name.equals(MAPPING_SELL_ESSENCE)) {


            CollisionResults results;
            results = rayCasting();
            Inventory inventory;
            Essence essence;


            inventory = Configuration.getGamer().getInventory();
            if (!isPressed)
                switch (name) {
                    case MAPPING_BUY_ESSENCE:
                        essence = Essence.buy();
                        if (essence != null) {
                            inventory.addEssence(essence);
                        }
                        break;
                    case MAPPING_SELL_ESSENCE:
                        essence = inventory.getEssence();
                        if (essence != null)
                            essence.sell();
                        break;
                }


            if (results.size() > 0 && !isPressed) {
                Cell cell = getCell(results);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_EXTRACTION_ESSENCE:
                            if (cell.isOccupied() && cell.getBuilding() instanceof Tower &&
                                    ((Tower) cell.getBuilding()).getCore() != null) {
                                Tower tower = (Tower) cell.getBuilding();
                                inventory.addEssence(tower.getCore());
                                tower.extractionCore();
                            }
                            break;
                        case MAPPING_PUT_EXTRACTED_ESSENCE:
                            if (cell.isOccupied() && cell.getBuilding() instanceof Tower &&
                                    !inventory.empty()) {
                                Tower tower = (Tower) cell.getBuilding();
                                Essence core = inventory.getEssence();
                                tower.putCore(core);
                            }
                            break;
                    }
                }
            }
        }
    }
}
