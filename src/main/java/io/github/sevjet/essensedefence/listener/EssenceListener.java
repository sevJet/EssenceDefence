package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Building;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.field.*;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class EssenceListener implements ActionListener {

    private Essence bufEssence = null;
    private CollisionResults results;
    private Inventory inventory;

    protected void onPress(String name, float tpf) {
    }


    protected void onRelease(String name, float tpf) {
        switch (name) {
            case MAPPING_BUY_ESSENCE:
                if (bufEssence == null)
                    buyEssence();
                break;
            case MAPPING_SELL_ESSENCE:
                if (bufEssence == null)
                    sellEssence();
                break;
            case MAPPING_EXTRACTION_ESSENCE:
                if (bufEssence == null)
                    extractionEssence();
                break;
            case MAPPING_PUT_EXTRACTED_ESSENCE:
                if (bufEssence != null)
                    putEssence();
                break;
            case MAPPING_UPGRADE_ESSENCE:
                if (bufEssence == null)
                    upgradeEssence();
                break;
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUY_ESSENCE) ||
                name.equals(MAPPING_EXTRACTION_ESSENCE) ||
                name.equals(MAPPING_PUT_EXTRACTED_ESSENCE) ||
                name.equals(MAPPING_SELL_ESSENCE) ||
                name.equals(MAPPING_UPGRADE_ESSENCE)) {
            if (isPressed) {
                onPress(name, tpf);
            } else {
                onRelease(name, tpf);
            }

            inventory = Configuration.getGamer().getInventory();
            results = rayCasting();
        }
    }

    private void sellEssence() {
        results = rayCasting(getInventory());
        Essence essence = extractFromResults(results);
        if (essence != null) {
            essence.sell();
        }
        bufEssence = null;

        results = rayCasting();
        essence = extractFromResults(results);
        if (essence != null) {
            essence.sell();
        }
        bufEssence = null;
    }

    private void extractionEssence() {
        results = rayCasting(getInventory());
        Essence essence = extractFromResults(results);
        if (essence != null) {
            bufEssence = essence;
            return;
        }

        results = rayCasting();
        bufEssence = extractFromResults(results);
    }

    private void upgradeEssence() {
        results = rayCasting(getInventory(),getField());
        if (results.size() <= 0) {
            return;
        }
        Cell cell = getCell(results);
        if (cell == null) {
            return;
        }
        if (cell instanceof MapCell) {
            Building building = ((MapCell) cell).getContent();
            if (building instanceof Tower) {
                Essence essence = ((Tower) building).getCore();
                if (essence != null) {
                    essence.upgrade();
                }
            }
        }
        if (cell instanceof InventoryCell) {
            Essence essence = ((InventoryCell) cell).getContent();
            if (essence != null) {
                essence.upgrade();
            }
        }
    }

    private Essence extractFromResults(CollisionResults results) {
        Essence essence = null;

        if (results.size() > 0) {
            Cell cell = getCell(results);
            if (canExtractFrom(cell)) {
                essence = extractFromCell(cell);

                Configuration.getRootNode().attachChild(essence.getGeometry());
                essence.getGeometry().addControl(new EssenceMovementControl());
            }
        }

        return essence;
    }

    private boolean canExtractFrom(final Cell cell) {
        Field field = cell != null ? cell.getField() : null;
        if (field == null) {
            return false;
        }

        if (field instanceof MapField) {
            return cell.hasContent() && cell.getContent() instanceof Tower &&
                    ((Tower) cell.getContent()).getCore() != null;
        }
        if (field instanceof Inventory) {
            System.out.println("field instanceof Inventory");
            return cell.hasContent();
        }

        return false;
    }

    private Essence extractFromCell(Cell cell) {
        Field field = cell.getField();
        Essence essence = null;

        if (field instanceof MapField) {
            Tower tower = (Tower) cell.getContent();
            essence = tower.getCore();
            tower.extractCore();
        }
        if (field instanceof Inventory) {
            essence = inventory.takeEssence((InventoryCell) cell);
        }

        return essence;
    }

    private void buyEssence() {
        bufEssence = Essence.buy();

        boolean isPlaced = placeOnResults(rayCasting(getField(), getInventory()));
        if (!isPlaced && bufEssence != null) {
            bufEssence.sell();
            bufEssence = null;
        }
    }

    private boolean putEssence() {
        return placeOnResults(rayCasting(getField(), getInventory()));
    }

    private boolean placeOnResults(CollisionResults results) {
        if (bufEssence == null) {
            return false;
        }
        if (results.size() > 0) {
            Cell cell = getCell(results);
            if (canPlaceOn(cell)) {
                if (!placeOn(cell)) {
                    return false;
                }
                bufEssence.getGeometry().removeControl(EssenceMovementControl.class);
                bufEssence = null;
                return true;
            }
        }
        return false;
    }

    private boolean canPlaceOn(Cell cell) {
        if (cell == null ||
                cell.getGeometry() == null ||
                cell.getGeometry().getParent() == null ||
                cell.getGeometry().getParent().getParent() == null) {
            return false;
        }

        Field field = cell.getField();
        if (field instanceof MapField) {
            return cell.hasContent() &&
                    cell.getContent() instanceof Tower &&
                    ((Tower) cell.getContent()).isEmpty();
        }

        if (field instanceof Inventory) {
            return !cell.hasContent();
        }

        return false;
    }

    private boolean placeOn(Cell cell) {
        Field field = cell.getField();

        if (field instanceof MapField) {
            Tower tower = (Tower) cell.getContent();
            tower.putCore(bufEssence);
            return true;
        }
        if (field instanceof Inventory) {
            return inventory.addEssence(bufEssence, (InventoryCell) cell);
        }

        return false;
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
