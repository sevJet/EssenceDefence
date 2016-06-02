package io.github.sevjet.essencedefence.util;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import io.github.sevjet.essencedefence.GamePlayAppState;
import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.field.Cell;
import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.field.InventoryCell;
import io.github.sevjet.essencedefence.field.MapCell;

public class RayHelper {

    private static final Node EMPTY_NODE = new Node();

    @Deprecated
    public static CollisionResults rayCasting() {
        return rayCasting(getMapField());
    }

    @Deprecated
    public static CollisionResults rayCasting(Node... objects) {
        CollisionResults results = new CollisionResults();
        Ray ray = fromCursor();
        for (Node with : objects) {
            if (with != null) {
                with.collideWith(ray, results);
                if (results.size() > 0) {
                    break;
                }
            }
        }
        return results;
    }

    @Deprecated
    public static Cell getCell(CollisionResults results) {
        if (results != null && results.size() > 0 && results.getClosestCollision() != null) {
            Geometry target = results.getClosestCollision().getGeometry();
            Entity entity = target.getUserData("entity");
            if (entity != null && entity instanceof Cell) {
                return (Cell) entity;
            }
        }
        return null;
    }

    public static Entity collideClosest(Node... objects) {
        CollisionResults results = new CollisionResults();
        Ray ray = fromCursor();
        for (Node with : objects) {
            if (with != null) {
                if (with.collideWith(ray, results) > 0) {
                    Geometry target = results.getClosestCollision().getGeometry();
                    Object obj = target.getUserData("entity");
                    if (obj != null && obj instanceof Entity) {
                        return (Entity) obj;
                    }
                }
            }
        }
        return null;
    }

    public static Node getMapField() {
        return getMapField(MapCell.class);
    }

    public static Node getMapField(Class<? extends Entity> elementClass) {
        return getFieldElements(GamePlayAppState.field, elementClass);
    }

    public static Node getInventory() {
        return getInventory(InventoryCell.class);
    }

    public static Node getInventory(Class<? extends Entity> elementClass) {
        return getFieldElements(Configuration.getInventory(), elementClass);
    }

    public static Node getShop() {
        return getShop(InventoryCell.class);
    }

    public static Node getShop(Class<? extends Entity> elementClass) {
        return getFieldElements(Configuration.getShop(), elementClass);
    }

    public static Node getFieldElements(Field field, Class<? extends Entity> elementClass) {
        return field != null ? field.getObjects(elementClass) : EMPTY_NODE;
    }

    @Deprecated
    private static Ray fromCamera() {
        return new Ray(Configuration.getCam().getLocation(),
                Configuration.getCam().getDirection());
    }

    private static Ray fromCursor() {
        Vector2f click2d = Configuration.getInputManager().getCursorPosition();
        Vector3f click3d = Configuration.getCam().getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 0f);

        Vector3f dir = Configuration.getCam().getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 1f).
                subtract(click3d);

        return new Ray(click3d, dir);
    }

}
