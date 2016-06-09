package io.github.sevjet.essencedefence.util;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

import java.util.HashMap;
import java.util.Map;

import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.entity.building.Wall;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.Cell;
import io.github.sevjet.essencedefence.field.InventoryCell;
import io.github.sevjet.essencedefence.field.MapCell;

import static io.github.sevjet.essencedefence.util.Creator.myBox;
import static io.github.sevjet.essencedefence.util.Creator.myQuad;
import static io.github.sevjet.essencedefence.util.Creator.myShinySphere;

public class GeometryManager {

    private static GeometryManager _instance = new GeometryManager();
    private final Map<Class<? extends Entity>, Geometry> defaultGeometry;

    private GeometryManager() {
        defaultGeometry = new HashMap<>();

        defaultGeometry.put(Entity.class, myBox(1 / 4f, 1 / 8f, 1 / 16f, ColorRGBA.Red));
    }

    public static GeometryManager getInstance() {
        return _instance;
    }

    public static Geometry getDefault(Class<? extends Entity> clazz) {
        return getDefault(clazz, null);
    }

    public static Geometry getDefault(Class<? extends Entity> clazz, Geometry defaultValue) {
        Geometry needle = getInstance().defaultGeometry.getOrDefault(clazz, defaultValue);

        return needle != null ? needle.clone() : null;
    }

    public static Geometry setDefault(Class<? extends Entity> clazz, Geometry newValue) {
        GeometryManager instance = getInstance();
        Geometry oldValue = instance.defaultGeometry.get(clazz);
        instance.defaultGeometry.put(clazz, newValue.clone());
        return oldValue;
    }

    public static void init() {
        getInstance().defaultGeometry.clear();

        setDefault(Cell.class, myQuad(1, 1, "cell", ColorRGBA.Black));
        setDefault(MapCell.class, getDefault(Cell.class));
        setDefault(InventoryCell.class, getDefault(Cell.class));
        setDefault(Wall.class, myBox(1 / 2f, 1 / 2f, 1f, "wall", ColorRGBA.Cyan));
        setDefault(Tower.class, myBox(1f, 1f, 1.5f, "tower", ColorRGBA.Green));
        setDefault(Fortress.class, myBox(3 / 2f, 3 / 2f, 2f, "fortress", ColorRGBA.Gray));
        setDefault(Portal.class, myBox(1f, 1 / 2f, 1.5f, "portal", ColorRGBA.Magenta));
        setDefault(Monster.class, myBox(1 / 3f, 1 / 3f, 1 / 2f, "monster", ColorRGBA.Yellow));
        setDefault(Essence.class, myShinySphere(1 / 2f, "essence", ColorRGBA.randomColor()));
    }
}
