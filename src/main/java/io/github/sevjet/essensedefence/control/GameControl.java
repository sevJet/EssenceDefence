package io.github.sevjet.essensedefence.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.MapField;

import java.util.ArrayList;

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
                for (Portal portal :portals){
                    ArrayList<Monster> monsters = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        Monster monster = Monster.getDefaultMonster();
                        monster.upgrade(level);
                        monsters.add(monster);
                    }
                    WaveControl wave = new WaveControl(monsters);
                    wave.setDelay(3f);
                    wave.setGap(2f);
                    portal.addWave(wave);
//                    level += 3;
                    level *= 2;
                    System.out.println(level);
                }
                portals.forEach(Portal::pushWave);
            }
        }
    }
    private long level = 10;

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

}
