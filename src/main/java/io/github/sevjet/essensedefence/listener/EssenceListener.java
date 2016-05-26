package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;
import de.lessvoid.nifty.elements.Element;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.EssenceShop;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.Inventory;
import io.github.sevjet.essensedefence.niftyGui.InfoScreen;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class EssenceListener implements ActionListener {

    public static InfoScreen info = null;
    private Essence bufEssence = null;
    private Cell lastCell = null;
    private CollisionResults results;
    private Inventory inventory;

    protected void onPress(String name, float tpf) {
        switch (name) {
            case MAPPING_EXTRACTION_ESSENCE:
                if (bufEssence == null) {
                    extractionEssence();
                }
                break;
        }

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
            case MAPPING_PUT_EXTRACTED_ESSENCE:
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
            case MAPPING_UPGRADE_ESSENCE:
                if (bufEssence == null)
                    upgradeEssence();
                break;
            case MAPPING_COMBINE_ESSENCE:
                if (bufEssence != null) {
                    combineEssence();
                }
                break;
            case MAPPING_INFO:
                infoAbout();
                break;
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUY_ESSENCE) ||
                name.equals(MAPPING_EXTRACTION_ESSENCE) ||
                name.equals(MAPPING_PUT_EXTRACTED_ESSENCE) ||
                name.equals(MAPPING_SELL_ESSENCE) ||
                name.equals(MAPPING_UPGRADE_ESSENCE) ||
                name.equals(MAPPING_COMBINE_ESSENCE) ||
                name.equals(MAPPING_INFO)) {
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

        boolean isPlaced = placeOnResults(rayCasting(getField(), getInventory()));
        if (!isPlaced && bufEssence != null) {
            bufEssence.sell();
            bufEssence = null;
        }
    }

    private void sellEssence() {
        results = rayCasting(getInventory(), getField());
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
        results = rayCasting(getInventory(), getField(), getShop());
        Essence essence = extractFromResults(results);
        bufEssence = essence;
        if (lastCell != null &&
                lastCell.getField() instanceof EssenceShop) {
            if (essence != null &&
                    !Configuration.getGamer().decGold(essence.getPrice())) {
                putEssence();
            }
        }
    }

    private boolean putEssence() {
        return placeOnResults(rayCasting(getField(), getInventory(), getShop()));
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
            Cell cell = getCell(results);
            Field field = cell.getField();
            if (field.canSet(cell, Essence.class)) {
                Essence localBuf = bufEssence;
                clearBuf();
                if (!field.setContent(cell, localBuf)) {
                    return false;
                }
                return true;
            }
            if (field instanceof EssenceShop) {
                bufEssence.sell();
                clearBuf();
                return true;
            }
        }
        return false;
    }

    private Essence extractFromResults(CollisionResults results) {
        Essence essence = null;
        if (results.size() > 0) {
            Cell cell = getCell(results);
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
