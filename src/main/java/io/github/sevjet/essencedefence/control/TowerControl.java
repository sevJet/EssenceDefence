package io.github.sevjet.essencedefence.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.IOException;

import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.util.Creator;


public class TowerControl extends BasicControl {

    private Node beamNode = null;
    private Tower tower = null;
    private float counter = 0f;

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);

        if (entity instanceof Tower) {
            tower = (Tower) entity;
        } else {
            throw new IllegalArgumentException("Not a Tower spatial");
        }

    }

    @Override
    protected void controlUpdate(float tpf) {
        if (beamNode == null) {
            beamNode = new Node("beamNode");
            tower.getField().attachChild(beamNode);
        }

        if (tower.isEmpty()) {
            beamNode.detachAllChildren();
            return;
        }
        counter += tpf;
        if (counter > tower.getCooldownTime() * 0.5f) {
            beamNode.detachAllChildren();
        }

        if (counter > tower.getCooldownTime()) {
            counter = 0f;

            final Vector3f start = tower.getCore().getCenter();
            tower.getCloseMonsters().stream()
                    .limit(tower.getCore().getMonsters())
                    .forEach(monster -> {
                        final Vector3f end = monster.getPhysicalCenter();
                        beamNode.attachChild(Creator.myLine(start, end, tower.getCore().getColor(), 33f));

                        monster.hit(tower.getCore().getDamage());
                    });
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(beamNode, "beamNode", null);
        capsule.write(tower, "tower", null);
        capsule.write(counter, "counter", 0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        beamNode = (Node) capsule.readSavable("beamNode", null);
        tower = (Tower) capsule.readSavable("tower", null);
        counter = capsule.readFloat("counter", 0f);

    }

}
