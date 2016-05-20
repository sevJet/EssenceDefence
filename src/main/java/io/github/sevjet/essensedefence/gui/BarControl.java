package io.github.sevjet.essensedefence.gui;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class BarControl extends AbstractControl {
    private ITextual object;
    private float max;
    private float now;
    private DataBar bar;
    private BarMode mode = BarMode.StartMax;

    public BarControl(ITextual entity) {
        this.object = entity;

        max = Float.parseFloat(object.outputValue());
        now = max;
    }

    public BarControl(ITextual entity, BarMode mode) {
        this(entity);
        this.mode = mode;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
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
        useMode(mode, now);
        float ratio = now / max;
        bar.setRatio(ratio);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    protected void useMode(BarMode mode, float now) {
        switch (mode) {
            case StartMax:
                if (now > max)
                    setMax(now);
                break;
            case EndlessLog10:
                setMax(1 + (float) Math.pow(10, Math.ceil(Math.log10(now))));
                break;
            case EndlessX2:
                if (now > max)
                    setMax(2 * max);
                break;
        }
    }
}

