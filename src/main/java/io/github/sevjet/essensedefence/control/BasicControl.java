package io.github.sevjet.essensedefence.control;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Entity;

public abstract class BasicControl extends AbstractControl {
    protected Entity entity;

    @Override
    public void setSpatial(Spatial spatial) {
        Entity entity = spatial.getUserData("entity");
        if (entity == null) {
            throw new IllegalArgumentException("Not an Entity spatial");
        }
        super.setSpatial(spatial);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
