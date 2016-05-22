package io.github.sevjet.essensedefence.listener;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.Configuration;

public class FollowControl extends AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {
        getSpatial().setLocalTranslation(Configuration.getCam().getLocation().add(
                Configuration.getCam().getDirection().mult(15)
        ));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
