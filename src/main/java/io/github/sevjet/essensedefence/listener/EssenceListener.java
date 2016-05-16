package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.gui.Inventory;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class EssenceListener implements ActionListener {

    private Essence bufEssence = null;


    private CollisionResults results;
    private Inventory inventory;

    protected void onPress(String name, float tpf) {

    }


    protected void onUnpress(String name, float tpf) {
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
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUY_ESSENCE) ||
                name.equals(MAPPING_EXTRACTION_ESSENCE) ||
                name.equals(MAPPING_PUT_EXTRACTED_ESSENCE) ||
                name.equals(MAPPING_SELL_ESSENCE)) {
            if (isPressed) {
                onPress(name, tpf);
            } else {
                onUnpress(name, tpf);
            }

            inventory = Configuration.getGamer().getInventory();
            results = rayCasting();
        }
    }

    //====================================== sell and extract =============================================================
    private void sellEssence() {
        results = rayCasting(Configuration.getGamer().getInventory().getObjects(Cell.class));
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
        results = rayCasting(Configuration.getGamer().getInventory().getObjects(Cell.class));
        Essence essence = extractFromResults(results);
        if (essence != null) {
            bufEssence = essence;
            return;
        }

        results = rayCasting();
        bufEssence = extractFromResults(results);
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

    private boolean canExtractFrom(Cell cell) {
        // TODO: 15/05/2016 use cell.getField()
        if (cell == null ||
                cell.getGeometry() == null ||
                cell.getGeometry().getParent() == null ||
                cell.getGeometry().getParent().getParent() == null)
            return false;
        Node fieldNode = cell.getGeometry().getParent().getParent();

        if (fieldNode instanceof Field) {
            return cell.isOccupied() && cell.getBuilding() instanceof Tower &&
                    ((Tower) cell.getBuilding()).getCore() != null;
        }
        if (fieldNode instanceof Inventory) {
            System.out.println("fieldNode instanceof Inventory");
            return inventory.isOccupied(cell);
        }

        return false;
    }

    private Essence extractFromCell(Cell cell) {
        // TODO: 15/05/2016 use cell.getField()
        Node fieldNode = cell.getGeometry().getParent().getParent();
        Essence essence = null;

        if (fieldNode instanceof Field) {
            Tower tower = (Tower) cell.getBuilding();
            essence = tower.getCore();
            tower.extractionCore();
        }
        if (fieldNode instanceof Inventory) {
            essence = inventory.getEssence(cell);
            inventory.removeObject(essence);
        }

        return essence;
    }

    //====================================================== buy and put ===================================================
    private void buyEssence() {
        CollisionResults results;
        bufEssence = Essence.buy();
        boolean isPlaced = false;

        results = rayCasting(Configuration.getGamer().getInventory().getObjects(Cell.class));
        isPlaced = placeOnResults(results);
        if (isPlaced)
            return;
        results = rayCasting();
        isPlaced = placeOnResults(results);
        if (!isPlaced && bufEssence != null) {
            bufEssence.sell();
            bufEssence = null;
        }
    }

    private void putEssence() {
        CollisionResults results;
        boolean isPlaced = false;

        results = rayCasting(Configuration.getGamer().getInventory().getObjects(Cell.class));
        isPlaced = placeOnResults(results);
        if (isPlaced)
            return;
        placeOnResults(rayCasting());
    }

    private boolean placeOnResults(CollisionResults results) {
        if (bufEssence == null)
            return false;
        if (results.size() > 0) {
            Cell cell = getCell(results);
            if (canPlaceOn(cell)) {
                if (!placeOn(cell))
                    return false;
                bufEssence.getGeometry().removeControl(EssenceMovementControl.class);

                bufEssence = null;
                return true;
            }
        }
        return false;
    }

    private boolean canPlaceOn(Cell cell) {
        // TODO: 15/05/2016 use cell.getField 
        if (cell == null ||
                cell.getGeometry() == null ||
                cell.getGeometry().getParent() == null ||
                cell.getGeometry().getParent().getParent() == null)
            return false;

        Node fieldNode = cell.getGeometry().getParent().getParent();
        if (fieldNode instanceof Field)
            return cell.isOccupied() &&
                    cell.getBuilding() instanceof Tower &&
                    ((Tower) cell.getBuilding()).isEmpty();

        if (fieldNode instanceof Inventory)
            return !inventory.isOccupied(cell);

        return false;
    }

    private boolean placeOn(Cell cell) {
        // TODO: 15/05/2016 use cell.getField() 
        Node fieldNode = cell.getGeometry().getParent().getParent();

        if (fieldNode instanceof Field) {
            Tower tower = (Tower) cell.getBuilding();
            tower.putCore(bufEssence);
            return true;
        }
        if (fieldNode instanceof Inventory) {
            return inventory.addEssence(bufEssence, cell);
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
