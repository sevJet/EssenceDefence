package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.gui.Inventory;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Getter;

import static io.github.sevjet.essensedefence.GamePlayAppState.field;
import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class EssenceListener implements ActionListener {
    private Essence bufEssence = null;

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
                        results = rayCasting(Configuration.getGamer().getInventory());
                        if (essence != null) {
                            if (results.size() == 0) {
                                inventory.addEssence(essence);
                            } else {
                                Cell cell2 = getCell(results);
                                inventory.addEssence(essence, cell2);
                            }
                        }
                        break;
                    case MAPPING_SELL_ESSENCE:
                        results = rayCasting(Configuration.getGamer().getInventory());
                        if (results.size() > 0) {
//                            Cell cell2 = getCell(results);
                            Geometry geom = results.getClosestCollision().getGeometry();
                            Entity entity = Getter.getEntity(geom);
                            if (entity != null) {
                                essence = null;
                                if (entity instanceof Essence) {
                                    essence = inventory.getEssence((Essence) entity);
                                }
                                if (entity instanceof Cell) {
                                    essence = inventory.getEssence((Cell) entity);
                                }
                                if (essence != null) {
                                    essence.sell();
                                }
                            }
                        }
//                        essence = inventory.getEssence();
//                        if (essence != null)
//                            essence.sell();
                        break;
                }


            if (!isPressed) {
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_EXTRACTION_ESSENCE:
                            if (results.size() > 0) {
                                Cell cell = getCell(results);
                                Field field = cell.getField();

                                if (cell.isOccupied() && cell.getBuilding() instanceof Tower &&
                                        ((Tower) cell.getBuilding()).getCore() != null) {
                                    Tower tower = (Tower) cell.getBuilding();
                                    bufEssence = tower.getCore();
                                    tower.extractionCore();

                                    Configuration.getRootNode().attachChild(bufEssence.getGeometry());
                                    bufEssence.getGeometry().addControl(new EssenceMovementControl());
                                }
                            }
                            results = rayCasting(Configuration.getGamer().getInventory());
                            if (results.size() > 0) {
                                Geometry geom = results.getClosestCollision().getGeometry();
                                Entity entity = Getter.getEntity(geom);
                                if (entity != null) {
                                    essence = null;
                                    if (entity instanceof Essence) {
                                        essence = inventory.getEssence((Essence) entity);
                                    }
                                    if (entity instanceof Cell) {
                                        essence = inventory.getEssence((Cell) entity);
                                    }
                                    if (essence != null) {
                                        bufEssence = essence;
                                        Configuration.getRootNode().attachChild(bufEssence.getGeometry());
                                        bufEssence.getGeometry().addControl(new EssenceMovementControl());
                                    }
                                }
                            }
                            break;
                        case MAPPING_PUT_EXTRACTED_ESSENCE:
                            if (bufEssence == null)
                                break;

                            results = rayCasting(Configuration.getGamer().getInventory());
                            if (results.size() > 0) {
                                Cell cell2 = getCell(results);
                                bufEssence.getGeometry().removeControl(AbstractControl.class);
                                bufEssence.getGeometry().removeFromParent();

                                inventory.addEssence(bufEssence, cell2);
                                bufEssence = null;
                            }
                            results = rayCasting();
                            if (results.size() > 0) {
                                Cell cell = getCell(results);
                                Field field = cell.getField();

                                if (cell.isOccupied() && cell.getBuilding() instanceof Tower) {
                                    Tower tower = (Tower) cell.getBuilding();

                                    bufEssence.getGeometry().removeControl(EssenceMovementControl.class);
                                    bufEssence.getGeometry().removeFromParent();

                                    tower.putCore(bufEssence);
                                    bufEssence = null;
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}

class EssenceMovementControl extends AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {
        getSpatial().setLocalTranslation(Configuration.getCam().getLocation().add(
                Configuration.getCam().getDirection().mult(15)
        ));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
