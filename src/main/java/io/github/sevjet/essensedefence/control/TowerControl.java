package io.github.sevjet.essensedefence.control;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.util.Creator;

import java.util.List;


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
//            // TODO: 12/05/2016 create new control
//            beamNode.addControl(new AbstractControl() {
//                private float counter = 0;
//
//                @Override
//                protected void controlUpdate(float tpf) {
//                    counter += tpf;
//                    if (counter > 1.3f) {
//                        counter = 0;
//                        System.out.println("-- detach beam");
//                        beamNode.detachAllChildren();
//                    }
//                }
//
//                @Override
//                protected void controlRender(RenderManager rm, ViewPort vp) {
//
//                }
//            });
            ones = true;
        }


        Vector3f start, end;
        if (tower.getCore() == null) {
            return;
        }
        counter += tpf;
        if (counter > 0.5 * tower.getCore().getSpeed())
            beamNode.detachAllChildren();
        // FIXME: 12/05/2016 CHANGE speed on cooldown
        if (counter > tower.getCore().getSpeed()) {
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
