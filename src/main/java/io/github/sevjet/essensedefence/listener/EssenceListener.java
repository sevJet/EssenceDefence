package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.elements.Element;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.Inventory;
import io.github.sevjet.essensedefence.niftyGui.InfoScreen;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class EssenceListener implements ActionListener {

    public static InfoScreen info = null;
    private Essence bufEssence = null;
    private CollisionResults results;
    private Inventory inventory;

    protected void onPress(String name, float tpf) {
        switch (name) {
            case MAPPING_EXTRACTION_ESSENCE:
                if (bufEssence == null) {
                    extractionEssence();
//                    Essence local = bufEssence;
//                    putEssence();
//                    System.out.println(local.getGeometry().getLocalTranslation());
//                    extractionEssence();
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
//            case MAPPING_EXTRACTION_ESSENCE:
//                if (bufEssence == null)
//                    extractionEssence();
//                break;
            case MAPPING_PUT_EXTRACTED_ESSENCE:
                if (bufEssence != null) {
                    putEssence();

                }
                break;
            case MAPPING_UPGRADE_ESSENCE:
                if (bufEssence == null)
                    upgradeEssence();
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
        putEssence();

        if (essence != null) {
            int x, y;
            Vector3f vec = Configuration.getCam().getScreenCoordinates(
                    essence.getGeometry().getWorldTranslation()
            );
            x = ((int) vec.getX());
            y = ((int) vec.getY());

//            InfoScreen info = new InfoScreen("interface/testNifty.xml");
            if (info == null)
                info = new InfoScreen("interface/mainMenu.xml", "start2");
            Element txt = info.getElement("txt");
            if (txt == null)
                return;
            txt.hide();
            info.setText(txt, "Level: " + essence.getLevel() + '\n' +
                    "Damage: " + essence.getDamage() + '\n' +
                    "Range: " + essence.getRange() + '\n' +
                    "Speed: " + essence.getSpeed() + '\n' +
                    "Price (buy): " + essence.getPrice());
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

    private void extractionEssence() {
        results = rayCasting(getInventory(), getField());
        Essence essence = extractFromResults(results);
        bufEssence = essence;
    }

    private boolean putEssence() {
        return placeOnResults(rayCasting(getField(), getInventory()));
    }

    private Essence extractFromResults(CollisionResults results) {
        Essence essence = null;
        if (results.size() > 0) {
            Cell cell = getCell(results);
            Field field = cell.getField();

            if (field.canGet(cell, Essence.class)) {
                essence = (Essence) field.getContent(cell, Essence.class);

                Configuration.getRootNode().attachChild(essence.getGeometry());
                essence.getGeometry().addControl(new FollowControl());
            }
        }

        return essence;
    }

    private boolean placeOnResults(CollisionResults results) {
        if (bufEssence == null) {
            return false;
        }
        if (results.size() > 0) {
            Cell cell = getCell(results);
            Field field = cell.getField();
            if (field.canSet(cell, Essence.class)) {
                if (!field.setContent(cell, bufEssence)) {
                    return false;
                }
                clearBuf();
                return true;
            }
        }
        return false;
    }

    private void clearBuf() {
        bufEssence.getGeometry().removeControl(FollowControl.class);
        bufEssence = null;
    }
}
