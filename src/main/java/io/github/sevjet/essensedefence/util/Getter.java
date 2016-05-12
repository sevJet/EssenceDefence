package io.github.sevjet.essensedefence.util;

import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.entity.Entity;

public final class Getter {
    public static Entity getEntity(Spatial spat) {
        return spat.getUserData("entity");
    }
}
