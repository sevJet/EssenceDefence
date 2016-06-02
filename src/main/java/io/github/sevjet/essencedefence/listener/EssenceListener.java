package io.github.sevjet.essencedefence.listener;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

import de.lessvoid.nifty.elements.Element;

import io.github.sevjet.essencedefence.control.FollowControl;
import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.field.Inventory;
import io.github.sevjet.essencedefence.field.InventoryCell;
import io.github.sevjet.essencedefence.niftyGui.InfoScreen;
import io.github.sevjet.essencedefence.util.Configuration;
import io.github.sevjet.essencedefence.util.RayHelper;

public class EssenceListener implements ActionListener {

    public static InfoScreen info = null;

    private final Node hand;
    private Essence handEssence = null;

    public EssenceListener() {
        hand = new Node("hand");
        Configuration.getRootNode().attachChild(hand);
    }

    protected void onPress(String name) {
        switch (name) {
            case ListenerManager.MAPPING_EXTRACTION_ESSENCE:
                takeEssence();
                break;
        }

    }

    protected void onRelease(String name) {
        switch (name) {
            case ListenerManager.MAPPING_BUY_ESSENCE:
                buyEssence();
                break;
            case ListenerManager.MAPPING_SELL_ESSENCE:
                sellEssence();
                break;
            case ListenerManager.MAPPING_PUT_EXTRACTED_ESSENCE:
                putEssence();
                break;
            case ListenerManager.MAPPING_UPGRADE_ESSENCE:
                upgradeEssence();
                break;
            case ListenerManager.MAPPING_COMBINE_ESSENCE:
                combineEssence();
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
                onPress(name);
            } else {
                onRelease(name);
            }
        }
    }

    private void infoAbout() {
        final Entity entity = RayHelper.collideClosest(
                RayHelper.getMapField(Essence.class),
                RayHelper.getInventory(Essence.class),
                RayHelper.getShop(Essence.class));
        if (entity != null) {
            final Essence essence = (Essence) entity;

            int x, y;
            Vector2f vec = Configuration.getInputManager().getCursorPosition();
            x = ((int) vec.getX());
            y = Configuration.getSettings().getHeight() - ((int) vec.getY());

            if (info == null) {
                info = new InfoScreen("interface/mainMenu.xml", "start2");
            }
            Element txt = info.getElement("txt");
            if (txt == null) {
                return;
            }
            txt.hide();
            info.setText(txt, essence.getInfo());
            info.moveTo(txt, x, y);
            info.update(txt);
            info.showAll();
        }

    }

    private void buyEssence() {
        if (handEssence != null) {
            return;
        }

        final Essence essence = Essence.buy();
        if (essence == null) {
            return;
        }

        final Entity entity = RayHelper.collideClosest(
                RayHelper.getMapField(Tower.class),
                RayHelper.getInventory());
        if (entity != null) {
            if (entity instanceof Tower) {
                final Tower tower = (Tower) entity;
                if (tower.isEmpty()) {
                    tower.putCore(essence);
                    return;
                }
            } else if (entity instanceof InventoryCell) {
                final InventoryCell cell = (InventoryCell) entity;
                if (!cell.hasContent()) {
                    cell.setContent(essence);
                    return;
                }
            }
        }
        essence.sell();
    }

    private void sellEssence() {
        if (handEssence != null) {
            return;
        }
        final Entity entity = RayHelper.collideClosest(
                RayHelper.getMapField(Tower.class),
                RayHelper.getInventory());
        if (entity != null) {
            if (entity instanceof Tower) {
                final Tower tower = (Tower) entity;
                if (!tower.isEmpty()) {
                    tower.extractCore()
                            .sell();
                }
            } else if (entity instanceof InventoryCell) {
                final InventoryCell cell = (InventoryCell) entity;
                if (cell.hasContent()) {
                    cell.getContent()
                            .sell();
                    cell.setContent(null);
                }
            }
        }
    }

    private void upgradeEssence() {
        if (handEssence == null) {
            final Entity entity = RayHelper.collideClosest(
                    RayHelper.getMapField(Tower.class),
                    RayHelper.getInventory());
            if (entity != null) {
                if (entity instanceof Tower) {
                    final Tower tower = (Tower) entity;
                    if (!tower.isEmpty()) {
                        tower.getCore()
                                .upgrade();
                    }
                } else if (entity instanceof InventoryCell) {
                    final InventoryCell cell = (InventoryCell) entity;
                    if (cell.hasContent()) {
                        cell.getContent()
                                .upgrade();
                    }
                }
            }
        } else {
            handEssence.upgrade();
        }
    }

    private void combineEssence() {
        if (handEssence == null) {
            return;
        }

        Essence essence = null;
        final Entity entity = RayHelper.collideClosest(
                RayHelper.getMapField(Tower.class),
                RayHelper.getInventory());
        if (entity != null) {
            if (entity instanceof Tower) {
                final Tower tower = (Tower) entity;
                if (!tower.isEmpty()) {
                    essence = tower.getCore();
                }
            } else if (entity instanceof InventoryCell) {
                final InventoryCell cell = (InventoryCell) entity;
                if (cell.hasContent()) {
                    essence = cell.getContent();
                }
            }
        }

        if (essence != null) {
            if (essence.combine(handEssence)) {
                clearHand();
            }
        }
    }

    private void takeEssence() {
        if (handEssence != null) {
            return;
        }

        final Entity entity = RayHelper.collideClosest(
                RayHelper.getMapField(Tower.class),
                RayHelper.getInventory(InventoryCell.class),
                RayHelper.getShop(Essence.class));
        if (entity != null) {
            if (entity instanceof Tower) {
                final Tower tower = (Tower) entity;
                handEssence = tower.extractCore();
            } else if (entity instanceof InventoryCell) {
                final InventoryCell cell = (InventoryCell) entity;
                handEssence = cell.extractEssence();
            } else if (entity instanceof Essence) {
                final Essence essence = (Essence) entity;
                handEssence = essence.buySame();
            }
        }

        if (handEssence != null) {
            attachHand();
        }
    }

    private void putEssence() {
        if (handEssence == null) {
            return;
        }

        boolean isPlaced = false;
        Entity entity = RayHelper.collideClosest(
                RayHelper.getMapField(Tower.class),
                RayHelper.getInventory());
        if (entity != null) {
            if (entity instanceof Tower) {
                final Tower tower = (Tower) entity;
                if (tower.isEmpty()) {
                    tower.putCore(handEssence);
                    isPlaced = true;
                }
            } else if (entity instanceof InventoryCell) {
                final InventoryCell cell = (InventoryCell) entity;
                final Inventory inventory = cell.getField();
                isPlaced = inventory.addEssence(handEssence, cell);
            }
        } else {
            entity = RayHelper.collideClosest(RayHelper.getShop());
            if (entity != null && entity instanceof InventoryCell) {
                handEssence.sell();
                isPlaced = true;
            }
        }

        if (isPlaced) {
            clearHand();
        }
    }

    private void clearHand() {
        if (handEssence == null) {
            return;
        }

        detachHand();
        handEssence = null;
    }

    private void attachHand() {
        hand.attachChild(handEssence.getGeometry());

        handEssence.getGeometry().addControl(new FollowControl());
    }

    private void detachHand() {
        hand.detachChild(handEssence.getGeometry());

        handEssence.getGeometry().removeControl(FollowControl.class);
    }

}
