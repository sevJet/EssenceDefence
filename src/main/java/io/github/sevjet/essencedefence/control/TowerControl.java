package io.github.sevjet.essencedefence.control;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.List;

import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.util.Creator;


public class TowerControl extends BasicControl {
    boolean ones = false;
    //    TODO: 12/05/16 Savable
    //    // TODO: 12/05/2016 move to AppState
//    private static Node beamNode = new Node("beamNode");
    private Node beamNode = new Node("beamNode");
    private Tower tower = null;
    private List<Monster> list = null;
    private float counter = 0;

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
        if (!ones) {
            tower.getField().attachChild(beamNode);
            ones = true;
        }

        Vector3f start, end;
        if (tower.isEmpty()) {
            beamNode.detachAllChildren();
            return;
        }
        counter += tpf;
        if (counter > 0.5 * tower.getCooldownTime()) {
            beamNode.detachAllChildren();
        }

        if (counter > tower.getCooldownTime()) {
            counter = 0;
            list = tower.getCloseMonsters();
            if (list.size() > 0) {
                start = tower.getCore().getCenter();
                end = list.get(0).getPhysicalCenter();
                list.get(0).hit(tower.getCore().getDamage());
                beamNode.attachChild(Creator.myLine(start, end, ColorRGBA.Red, 33f));
            }
        }
    }
}
