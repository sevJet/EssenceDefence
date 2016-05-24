package io.github.sevjet.essensedefence.listener;

import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.util.Configuration;

public class FollowControl extends AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {
        Vector2f click2d = Configuration.getInputManager().getCursorPosition();
        Vector2f center = new Vector2f(
                Configuration.getSettings().getWidth(),
                Configuration.getSettings().getHeight()).divide(2f);
        float dist = 13f;

//        getSpatial().setLocalTranslation(Configuration.getCam().getLocation().add(
//                Configuration.getCam().getDirection().mult(15)
//        ));

        getSpatial().setLocalTranslation(Configuration.getCam().getWorldCoordinates(click2d.subtract(center).mult(dist), 0f).
                add(Configuration.getCam().getDirection().mult(dist)));

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
