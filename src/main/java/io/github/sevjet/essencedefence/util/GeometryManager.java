package io.github.sevjet.essencedefence.util;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

import java.util.HashMap;
import java.util.Map;

import io.github.sevjet.essencedefence.entity.Entity;

import static io.github.sevjet.essencedefence.util.Creator.myBox;

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
}
