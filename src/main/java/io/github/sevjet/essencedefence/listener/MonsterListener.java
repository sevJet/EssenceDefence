package io.github.sevjet.essencedefence.listener;

import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.ArrayList;

import io.github.sevjet.essencedefence.control.WaveControl;
import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.RayHelper;

public class MonsterListener implements ActionListener {

    private int level = 1;

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_SPAWN_MONSTER) ||
                name.equals(ListenerManager.MAPPING_SPAWN_WAVE) ||
                name.equals(ListenerManager.MAPPING_SPAWN_ALL)) {
            Monster monster;


            if (!isPressed) {
                final Entity entity = RayHelper.collideClosest(RayHelper.getMapField());
                MapCell cell = null;
                MapField field = null;
                if (entity != null && entity instanceof MapCell) {
                    cell = (MapCell) entity;
                    field = cell.getField();
                }

                if (field != null) {
                    switch (name) {
                        case ListenerManager.MAPPING_SPAWN_MONSTER:
                            if (!cell.hasContent()) {
                                monster = Monster.getDefaultMonster();
                                monster.move(cell.getX(), cell.getY());
                                field.addObject(monster);
                            }
                            break;
                        case ListenerManager.MAPPING_SPAWN_WAVE:
                            if (cell.hasContent() && cell.getContent() instanceof Portal) {
                                Portal portal = (Portal) cell.getContent();
                                addWave(portal, level++);
                            }
                            break;
                        case ListenerManager.MAPPING_SPAWN_ALL:
                            addWave(field, level++);
                            break;
                    }
                }
            }
        }
    }

    private void addWave(final MapField field, final int level) {
        addWave(field, level, 15, 3f, 2f);
    }

    private void addWave(final MapField field, final int level, final int count, final float delay, final float gap) {
        Node portals = field.getObjects(Portal.class);
        if (portals != null) {
            for (final Spatial spatial : portals.getChildren()) {
                final Entity entity = spatial.getUserData("entity");
                if (entity != null) {
                    final Portal portal = (Portal) entity;
                    addWave(portal, level, count, delay, gap);
                }
            }
        }
    }

    private void addWave(final Portal portal, final int level) {
        addWave(portal, level, 15, 3f, 2f);
    }

    private void addWave(final Portal portal, final int level, final int count, final float delay, final float gap) {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Monster monster = Monster.getDefaultMonster();
            monster.upgrade(level);
            monsters.add(monster);
        }

        WaveControl wave = new WaveControl(monsters);
        wave.setDelay(delay);
        wave.setGap(gap);

        portal.addWave(wave);
    }
}
