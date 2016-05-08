package io.github.sevjet.essensedefence.control;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Entity;

public abstract class BasicControl extends AbstractControl {
    protected Entity object;

    @Override
    public void setSpatial(Spatial spatial) {
        Entity object = spatial.getUserData("jme3object");
        if (object == null) {
            throw new IllegalArgumentException("Not a Entity spatial");
        }
        super.setSpatial(spatial);
        this.object = object;
    }
}
