package io.github.sevjet.essensedefence.util.listeners;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.*;

public class ListenerForEssence implements ActionListener {
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUY_ESSENCE) ||
                name.equals(MAPPING_EXTRACTION_ESSENCE) ||
                name.equals(MAPPING_PUT_EXTRACTED_ESSENCE) ||
                name.equals(MAPPING_SELL_ESSENCE)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0 && !isPressed) {
                Cell cell = getCell(results);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_EXTRACTION_ESSENCE:
                            if (cell.getBuilding() != null && cell.getBuilding() instanceof Tower &&
                                    ((Tower) cell.getBuilding()).getCore() != null) {
                                Configuration.getGamer().getExtractedEssences().add(((Tower) cell.getBuilding()).getCore());
                                ((Tower) cell.getBuilding()).extractionCore();
                            }
                            break;
                        case MAPPING_BUY_ESSENCE:
                            if (cell.getBuilding() != null && cell.getBuilding() instanceof Tower &&
                                    ((Tower) cell.getBuilding()).getCore() == null) {
                                Essence essence = Essence.buy();
                                if (essence != null) {
                                    ((Tower) cell.getBuilding()).putCore(essence);
                                }
                            }
                            break;
                        case MAPPING_PUT_EXTRACTED_ESSENCE:
                            if (cell.getBuilding() != null && cell.getBuilding() instanceof Tower &&
                                    Configuration.getGamer().getExtractedEssences().size() != 0) {
                                ((Tower) cell.getBuilding()).putCore(Configuration.getGamer().getExtractedEssences().get(0));
                                Configuration.getGamer().getExtractedEssences().remove(0);
                            }
                            break;
                        case MAPPING_SELL_ESSENCE:
                            if (cell.getBuilding() != null && cell.getBuilding() instanceof Tower &&
                                    ((Tower) cell.getBuilding()).getCore() != null) {
                                Essence.sell((Tower) cell.getBuilding());
                            }
                    }
                }
            }
        }
    }
}
