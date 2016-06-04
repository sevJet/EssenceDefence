package io.github.sevjet.essencedefence.control;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.util.Creator;


public class TowerControl extends BasicControl {

    private boolean isAttached = false;
    //    TODO: 12/05/16 Savable
    //    // TODO: 12/05/2016 move to AppState
    private Node beamNode = new Node("beamNode");
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
        if (!isAttached) {
            tower.getField().attachChild(beamNode);
            isAttached = true;
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
}
