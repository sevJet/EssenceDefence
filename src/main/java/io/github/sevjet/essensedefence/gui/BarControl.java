package io.github.sevjet.essensedefence.gui;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.util.Getter;

public class BarControl extends AbstractControl {
    private ITextual object;
    private float max;
    private float now;
    private DataBar bar;

    public BarControl(Entity entity) {
        if (entity instanceof ITextual) {
            this.object = (ITextual) entity;
        } else {
            throw new ClassCastException(Getter.getEntity(getSpatial()).getClass().toString() +
                    " must implement ITextual interface!!!");
        }
        max = Float.parseFloat(object.outputValue());
        now = max;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (getSpatial() instanceof DataBar) {
            bar = (DataBar) getSpatial();
        } else {
            throw new ClassCastException(getSpatial().getClass().toString() +
                    " must be DataBar");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        now = Float.parseFloat(object.outputValue());
        float ratio = now / max;
        bar.setRatio(ratio);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
