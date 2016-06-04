package io.github.sevjet.essencedefence.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import java.io.IOException;
import java.util.ArrayList;

import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.field.MapField;

public class GameControl extends AbstractControl {

    private MapField field;
    private float gap = 0f;
    private float timer = -1f;

    public GameControl(float gap) {
        if (gap < 0f) {
            throw new IllegalArgumentException("Gap can not be less than zero");
        }
        this.gap = gap;
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if (spatial == null || !(spatial instanceof MapField)) {
            return;
        }
        field = (MapField) spatial;

        super.setSpatial(spatial);
    }

    @Override
    protected void controlUpdate(float tpf) {
        boolean isWaveEnded = true;
        ArrayList<Portal> portals = new ArrayList<>();
        Node portalNode = field.getObjects(Portal.class);
        if (portalNode != null) {
            portalNode.getChildren()
                    .forEach(portalSpatial -> portals.add(portalSpatial.getUserData("entity")));
        }
        if (portals.stream()
                .filter(Portal::isWaveActive)
                .findAny()
                .isPresent()) {
            isWaveEnded = false;
        }
        if (isWaveEnded) {
            if (timer == -1f) {
                timer = gap;
            } else {
                timer -= tpf;
            }
            if (timer <= 0f) {
                timer = -1f;

                portals.forEach(Portal::pushWave);
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(field, "field", null);
        capsule.write(gap, "gap", 0f);
        capsule.write(timer, "timer", -1f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        field = (MapField) capsule.readSavable("field", null);
        gap = capsule.readFloat("gap", 0f);
        timer = capsule.readFloat("timer", -1f);
    }
}
