package io.github.sevjet.essencedefence.util;

import com.jme3.scene.Spatial;
import io.github.sevjet.essencedefence.entity.Entity;

public final class Getter {
    public static Entity getEntity(Spatial spat) {
        return spat.getUserData("entity");
    }
}
