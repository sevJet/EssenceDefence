package io.github.sevjet.essencedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;
import de.lessvoid.nifty.elements.Element;
import io.github.sevjet.essencedefence.control.FollowControl;
import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.field.Cell;
import io.github.sevjet.essencedefence.field.EssenceShop;
import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.field.Inventory;
import io.github.sevjet.essencedefence.niftyGui.InfoScreen;
import io.github.sevjet.essencedefence.util.Configuration;

public class EssenceListener implements ActionListener {

    public static InfoScreen info = null;
    private Essence bufEssence = null;
    private Cell lastCell = null;
    private CollisionResults results;
    private Inventory inventory;

    protected void onPress(String name, float tpf) {
        switch (name) {
            case ListenerManager.MAPPING_EXTRACTION_ESSENCE:
                if (bufEssence == null) {
                    extractionEssence();
                }
                break;
        }

    }

    protected void onRelease(String name, float tpf) {
        switch (name) {
            case ListenerManager.MAPPING_BUY_ESSENCE:
                if (bufEssence == null)
                    buyEssence();
                break;
            case ListenerManager.MAPPING_SELL_ESSENCE:
                if (bufEssence == null)
                    sellEssence();
                break;
            case ListenerManager.MAPPING_PUT_EXTRACTED_ESSENCE:
                if (bufEssence != null) {
                    putEssence();
                    if (bufEssence != null && lastCell != null) {
                        if (lastCell.getField() instanceof EssenceShop) {
                            bufEssence.sell();
                            clearBuf();
                        } else {
                            putEssence(lastCell);
                        }
                        lastCell = null;
                    }
                }
                break;
            case ListenerManager.MAPPING_UPGRADE_ESSENCE:
                if (bufEssence == null)
                    upgradeEssence();
                break;
            case ListenerManager.MAPPING_COMBINE_ESSENCE:
                if (bufEssence != null) {
                    combineEssence();
                }
                break;
            case ListenerManager.MAPPING_INFO:
                infoAbout();
                break;
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_BUY_ESSENCE) ||
                name.equals(ListenerManager.MAPPING_EXTRACTION_ESSENCE) ||
                name.equals(ListenerManager.MAPPING_PUT_EXTRACTED_ESSENCE) ||
                name.equals(ListenerManager.MAPPING_SELL_ESSENCE) ||
                name.equals(ListenerManager.MAPPING_UPGRADE_ESSENCE) ||
                name.equals(ListenerManager.MAPPING_COMBINE_ESSENCE) ||
                name.equals(ListenerManager.MAPPING_INFO)) {
            if (isPressed) {
                onPress(name, tpf);
            } else {
                onRelease(name, tpf);
            }

            inventory = Configuration.getGamer().getInventory();
        }
    }

    private void infoAbout() {
        Essence essence;
        extractionEssence();
        essence = bufEssence;
        if (!putEssence())
            clearBuf();

        if (essence != null) {
            int x, y;
            Vector2f vec = Configuration.getInputManager().getCursorPosition();
            x = ((int) vec.getX());
            y = Configuration.getSettings().getHeight() - ((int) vec.getY());

            if (info == null)
                info = new InfoScreen("interface/mainMenu.xml", "start2");
            Element txt = info.getElement("txt");
            if (txt == null)
                return;
            txt.hide();
            info.setText(txt, essence.getInfo());
            info.moveTo(txt, x, y);
            info.update(txt);
            info.showAll();
        }

    }

    private void buyEssence() {
        bufEssence = Essence.buy();

        boolean isPlaced = placeOnResults(ListenerManager.rayCasting(ListenerManager.getField(), ListenerManager.getInventory()));
        if (!isPlaced && bufEssence != null) {
            bufEssence.sell();
            bufEssence = null;
        }
    }

    private void sellEssence() {
        results = ListenerManager.rayCasting(ListenerManager.getInventory(), ListenerManager.getField());
        Essence essence = extractFromResults(results);
        if (essence != null) {
            essence.sell();
        }
        bufEssence = null;
    }

    private void upgradeEssence() {
        extractionEssence();
        if (bufEssence != null)
            bufEssence.upgrade();
        putEssence();
    }

    private void combineEssence() {
        Essence extractionEssence = bufEssence;
        clearBuf();
        extractionEssence();
        Essence placedEssence = bufEssence;
        putEssence();
        if (placedEssence != null) {
            placedEssence.combine(extractionEssence);
        } else {
            bufEssence = extractionEssence;
            bufEssence.getGeometry().addControl(new FollowControl());
        }
    }

    private void extractionEssence() {
        results = ListenerManager.rayCasting(ListenerManager.getInventory(), ListenerManager.getField(), ListenerManager.getShop());
        Essence essence = extractFromResults(results);
        bufEssence = essence;

        // TODO: 27/05/2016 move to another method
        if (lastCell != null &&
                lastCell.getField() instanceof EssenceShop) {
            if (essence != null &&
                    !Configuration.getGamer().decGold(essence.getPrice())) {
                clearBuf();
            }
        }
    }

    private boolean putEssence() {
        return placeOnResults(ListenerManager.rayCasting(ListenerManager.getField(), ListenerManager.getInventory(), ListenerManager.getShop()));
    }

    // TODO: 26/05/2016 recreate
    private boolean putEssence(Cell lastCell) {
        if (bufEssence == null) {
            return false;
        }
        if (lastCell != null) {
            Cell cell = lastCell;
            Field field = cell.getField();
            if (field.canSet(cell, Essence.class)) {
                Essence localBuf = bufEssence;
                clearBuf();
                if (!field.setContent(cell, localBuf)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean placeOnResults(CollisionResults results) {
        if (bufEssence == null) {
            return false;
        }
        if (results.size() > 0) {
            Cell cell = ListenerManager.getCell(results);
            Field field = cell.getField();
            if (field.canSet(cell, Essence.class)) {
                Essence localBuf = bufEssence;
                clearBuf();
                if (!field.setContent(cell, localBuf)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private Essence extractFromResults(CollisionResults results) {
        Essence essence = null;
        if (results.size() > 0) {
            Cell cell = ListenerManager.getCell(results);
            lastCell = cell;
            Field field = cell.getField();

            if (field.canGet(cell, Essence.class)) {
                essence = (Essence) field.getContent(cell, Essence.class);

                Configuration.getRootNode().attachChild(essence.getGeometry());
                essence.getGeometry().addControl(new FollowControl());
            }
        }

        return essence;
    }

    private void clearBuf() {
        if (bufEssence == null)
            return;
        bufEssence.getGeometry().removeControl(FollowControl.class);
        bufEssence.getGeometry().removeFromParent();
        bufEssence = null;
    }
}
