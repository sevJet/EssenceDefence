package io.github.sevjet.essensedefence;

import com.jme3.scene.Geometry;

import java.util.HashMap;
import java.util.Map;

public class GeometryManager {

    private static GeometryManager _instance = new GeometryManager();

    public static GeometryManager getInstance() {
        return _instance;
    }

    private final Map<Class<? extends JME3Object>, Geometry> defaultGeometry;

    private GeometryManager() {
        defaultGeometry = new HashMap<>();
    }

    public static Geometry getDefault(Class<? extends JME3Object> clazz) {
        return getDefault(clazz, null);
    }

    public static Geometry getDefault(Class<? extends JME3Object> clazz, Geometry defaultValue) {
        Geometry needle = getInstance().defaultGeometry.getOrDefault(clazz, defaultValue);

        return needle != null ? needle.clone() : null;
    }

    public static Geometry setDefault(Class<? extends JME3Object> clazz, Geometry newValue) {
        GeometryManager instance = getInstance();
        Geometry oldValue = instance.defaultGeometry.get(clazz);
        instance.defaultGeometry.put(clazz, newValue.clone());
        return oldValue;
    }
}
